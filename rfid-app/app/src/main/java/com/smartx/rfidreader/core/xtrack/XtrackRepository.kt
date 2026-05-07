package com.smartx.rfidreader.core.xtrack

import android.util.Log
import android.util.Xml
import com.smartx.rfidreader.core.db.XtrackEventDao
import com.smartx.rfidreader.core.db.XtrackEventEntity
import com.smartx.rfidreader.core.db.XtrackLocationDao
import com.smartx.rfidreader.core.db.XtrackLocationEntity
import com.smartx.rfidreader.core.db.XtrackObjectDao
import com.smartx.rfidreader.core.db.XtrackObjectEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import java.util.Collections
import java.util.concurrent.atomic.AtomicInteger
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParser
import java.io.StringReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

/**
 * Responsável por toda a integração com o servidor Xtrack:
 *  - Requisições HTTP (GET/POST com XML)
 *  - Persistência local de objetos e localizações
 *  - Lookup rápido por EPC (usado em background durante leitura)
 */
class XtrackRepository(
    private val objectDao: XtrackObjectDao,
    private val locationDao: XtrackLocationDao,
    private val xtrackEventDao: XtrackEventDao
) {

    private val TAG = "XtrackRepository"

    private val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US).apply {
        timeZone = TimeZone.getDefault()
    }

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .dispatcher(okhttp3.Dispatcher().also {
            it.maxRequests = 32
            it.maxRequestsPerHost = 16
        })
        .build()

    // -------------------------------------------------------------------------
    // Flows para observação reativa
    // -------------------------------------------------------------------------

    val objectsFlow: Flow<List<XtrackObjectEntity>> = objectDao.allFlow()
    val locationsFlow: Flow<List<XtrackLocationEntity>> = locationDao.allFlow()
    val xtrackEventsFlow: Flow<List<XtrackEventEntity>> = xtrackEventDao.allFlow()
    val pendingXtrackCountFlow: Flow<Int> = xtrackEventDao.pendingCountFlow()

    // -------------------------------------------------------------------------
    // Lookup local (chamado em background pelo MainViewModel ao ler tag)
    // -------------------------------------------------------------------------

    suspend fun getObjectByEpc(epc: String): XtrackObjectEntity? =
        withContext(Dispatchers.IO) { objectDao.findByEpc(epc) }

    suspend fun getLocationById(id: String): XtrackLocationEntity? =
        withContext(Dispatchers.IO) { locationDao.findById(id) }

    suspend fun getAllLocationNames(): Map<String, String> =
        withContext(Dispatchers.IO) {
            locationDao.getAll().associate { it.id to it.name }
        }

    suspend fun getAllLocations(): List<XtrackLocationEntity> =
        withContext(Dispatchers.IO) { locationDao.getAll() }

    suspend fun getObjectsByLocation(locationId: String): List<XtrackObjectEntity> =
        withContext(Dispatchers.IO) { objectDao.findByLocation(locationId) }

    suspend fun objectCount(): Int = withContext(Dispatchers.IO) { objectDao.count() }
    suspend fun locationCount(): Int = withContext(Dispatchers.IO) { locationDao.count() }

    // -------------------------------------------------------------------------
    // Eventos Xtrack — persistência
    // -------------------------------------------------------------------------

    /**
     * Salva uma movimentação de local (change_location).
     * tags: lista de Triple(epc, idcode, description)
     */
    suspend fun saveChangeLocation(
        deviceId: String,
        locationId: String,
        locationName: String,
        tags: List<Triple<String, String, String>>
    ): Long = withContext(Dispatchers.IO) {
        val arr = JSONArray()
        tags.forEach { (epc, idcode, description) ->
            arr.put(JSONObject().apply {
                put("epc", epc)
                put("idcode", idcode)
                put("description", description)
            })
        }
        val entity = XtrackEventEntity(
            deviceId = deviceId,
            eventType = "change_location",
            locationId = locationId,
            locationName = locationName,
            tagsJson = arr.toString(),
            savedAt = isoFormat.format(Date())
        )
        xtrackEventDao.insert(entity)
    }

    /**
     * Salva (ou atualiza) um inventário de local na tabela de eventos Xtrack.
     */
    suspend fun saveXtrackLocationInventory(
        deviceId: String,
        locationId: String,
        locationName: String,
        total: Int,
        foundEpcs: List<String>,
        missingEpcs: List<String>,
        existingEventId: Long? = null
    ): Long = withContext(Dispatchers.IO) {
        val tagsJson = JSONObject().apply {
            put("location_id", locationId)
            put("location_name", locationName)
            put("total", total)
            put("found", foundEpcs.size)
            put("found_tags", JSONArray(foundEpcs))
            put("missing_tags", JSONArray(missingEpcs))
        }.toString()
        val timestamp = isoFormat.format(Date())

        if (existingEventId != null) {
            val existing = xtrackEventDao.findById(existingEventId)
            if (existing != null) {
                xtrackEventDao.update(
                    existing.copy(
                        tagsJson = tagsJson,
                        savedAt = timestamp,
                        isSynced = false,
                        syncedAt = ""
                    )
                )
                return@withContext existingEventId
            }
        }

        xtrackEventDao.insert(
            XtrackEventEntity(
                deviceId = deviceId,
                eventType = "location_inventory",
                locationId = locationId,
                locationName = locationName,
                tagsJson = tagsJson,
                savedAt = timestamp
            )
        )
    }

    suspend fun findExistingXtrackLocationInventory(locationId: String): XtrackEventEntity? =
        withContext(Dispatchers.IO) { xtrackEventDao.findPendingLocationInventory(locationId) }

    suspend fun deleteXtrackEvent(event: XtrackEventEntity) =
        withContext(Dispatchers.IO) { xtrackEventDao.delete(event) }

    suspend fun deleteAllXtrackEvents() =
        withContext(Dispatchers.IO) { xtrackEventDao.deleteAll() }

    // -------------------------------------------------------------------------
    // Eventos Xtrack — sincronização
    // -------------------------------------------------------------------------

    /**
     * Sincroniza todos os eventos Xtrack pendentes.
     *  - change_location  → chama MoveLocation no servidor Xtrack (por tag)
     *  - location_inventory → POST JSON ao webhookUrl
     * Remove o evento do banco apenas em caso de sucesso.
     */
    suspend fun syncXtrackEventsWithProgress(
        xtrackUrl: String,
        webhookUrl: String,
        maxConcurrentCalls: Int = 8,
        onLog: suspend (String) -> Unit = {},
        onProgress: suspend (
            current: Int,
            total: Int,
            event: XtrackEventEntity,
            success: Boolean,
            error: String?
        ) -> Unit
    ): Pair<Int, Int> {
        val pending = withContext(Dispatchers.IO) { xtrackEventDao.pending() }
        val total = pending.size
        if (total == 0) return Pair(0, 0)

        val successCount = AtomicInteger(0)
        val failCount = AtomicInteger(0)
        val processedCount = AtomicInteger(0)

        // Semáforo compartilhado limita total de chamadas HTTP simultâneas
        val semaphore = Semaphore(maxConcurrentCalls)

        onLog("=== ${total} evento(s) pendente(s) · concorrência: $maxConcurrentCalls ===")

        coroutineScope {
            pending.map { event ->
                async(Dispatchers.IO) {
                    val (ok, errMsg) = when (event.eventType) {
                        "change_location"    -> syncChangeLocationEvent(event, xtrackUrl, semaphore, onLog)
                        "location_inventory" -> syncLocationInventoryEvent(event, xtrackUrl, webhookUrl, semaphore, onLog)
                        else -> Pair(false, "Tipo desconhecido: ${event.eventType}")
                    }
                    if (ok) {
                        withContext(Dispatchers.IO) { xtrackEventDao.delete(event) }
                        successCount.incrementAndGet()
                    } else {
                        failCount.incrementAndGet()
                    }
                    val current = processedCount.incrementAndGet()
                    onProgress(current, total, event, ok, errMsg)
                }
            }.awaitAll()
        }

        return Pair(successCount.get(), failCount.get())
    }

    private suspend fun syncChangeLocationEvent(
        event: XtrackEventEntity,
        xtrackUrl: String,
        semaphore: Semaphore,
        onLog: suspend (String) -> Unit
    ): Pair<Boolean, String?> {
        if (xtrackUrl.isBlank()) return Pair(false, "URL do Xtrack não configurada")
        return try {
            val arr = JSONArray(event.tagsJson)
            val tagCount = arr.length()
            val tagOkCount = AtomicInteger(0)
            val tagFailCount = AtomicInteger(0)
            val errors = Collections.synchronizedList(mutableListOf<String>())

            onLog("▶ Movimentação '${event.locationName}' — $tagCount tag(s) em paralelo")

            coroutineScope {
                (0 until tagCount).map { i ->
                    async(Dispatchers.IO) {
                        val tagObj = arr.getJSONObject(i)
                        val idcode = tagObj.optString("idcode")
                        val epc = tagObj.optString("epc")
                        if (idcode.isBlank()) {
                            onLog("  [${i + 1}/$tagCount] sem idcode (epc=$epc), ignorado")
                            return@async
                        }
                        val xml = buildMoveLocationXml(idcode, event.locationName)
                        onLog("  → [${i + 1}/$tagCount] idcode=$idcode")
                        onLog("    payload: $xml")
                        semaphore.withPermit {
                            try {
                                val response = postXml(xtrackUrl, xml)
                                onLog("  ✓ [${i + 1}/$tagCount] resposta: $response")
                                tagOkCount.incrementAndGet()
                            } catch (e: Exception) {
                                onLog("  ✗ [${i + 1}/$tagCount] ERRO: ${e.message}")
                                errors.add("idcode=$idcode: ${e.message}")
                                tagFailCount.incrementAndGet()
                            }
                        }
                    }
                }.awaitAll()
            }

            val ok = tagOkCount.get()
            val fail = tagFailCount.get()
            val skipped = tagCount - ok - fail
            onLog("  Resultado: $ok ✓  $fail ✗${if (skipped > 0) "  $skipped ignorados" else ""}")

            if (fail == 0) Pair(true, null)
            else Pair(false, errors.firstOrNull() ?: "$fail tag(s) com falha")
        } catch (e: Exception) {
            Log.e(TAG, "syncChangeLocationEvent error", e)
            onLog("  ERRO fatal: ${e.message}")
            Pair(false, e.message)
        }
    }

    private suspend fun syncLocationInventoryEvent(
        event: XtrackEventEntity,
        xtrackUrl: String,
        webhookUrl: String,
        semaphore: Semaphore,
        onLog: suspend (String) -> Unit
    ): Pair<Boolean, String?> {
        val tagsData = JSONObject(event.tagsJson)
        val foundEpcs = tagsData.optJSONArray("found_tags")
            ?.let { arr -> (0 until arr.length()).map { arr.getString(it) } }
            ?: emptyList()

        onLog("▶ Inventário '${event.locationName}' — ${foundEpcs.size} tag(s) encontrada(s)")

        // ── 1. MoveLocation para cada tag encontrada (paralelo) ──────────────
        val moveOk = AtomicInteger(0)
        val moveFail = AtomicInteger(0)
        val moveErrors = Collections.synchronizedList(mutableListOf<String>())

        if (xtrackUrl.isNotBlank() && foundEpcs.isNotEmpty()) {
            onLog("  → Chamando MoveLocation para ${foundEpcs.size} tag(s)...")
            coroutineScope {
                foundEpcs.mapIndexed { i, epc ->
                    async(Dispatchers.IO) {
                        val obj = objectDao.findByEpc(epc)
                        val idcode = obj?.idcode ?: ""
                        if (idcode.isBlank()) {
                            onLog("  [${i + 1}/${foundEpcs.size}] sem idcode (epc=$epc), ignorado")
                            return@async
                        }
                        val xml = buildMoveLocationXml(idcode, event.locationName)
                        onLog("  → [${i + 1}/${foundEpcs.size}] MoveLocation idcode=$idcode")
                        semaphore.withPermit {
                            try {
                                val response = postXml(xtrackUrl, xml)
                                onLog("  ✓ [${i + 1}/${foundEpcs.size}] resposta: $response")
                                moveOk.incrementAndGet()
                            } catch (e: Exception) {
                                onLog("  ✗ [${i + 1}/${foundEpcs.size}] ERRO: ${e.message}")
                                moveErrors.add("idcode=$idcode: ${e.message}")
                                moveFail.incrementAndGet()
                            }
                        }
                    }
                }.awaitAll()
            }
            onLog("  MoveLocation: ${moveOk.get()} ✓  ${moveFail.get()} ✗")
        } else if (xtrackUrl.isBlank()) {
            onLog("  (URL Xtrack não configurada — MoveLocation ignorado)")
        }

        // ── 2. POST JSON ao webhook ───────────────────────────────────────────
        if (webhookUrl.isBlank()) {
            onLog("  (URL webhook não configurada — POST ignorado)")
            // Só falha se ambos estiverem em branco
            return if (xtrackUrl.isBlank()) Pair(false, "Nenhuma URL configurada")
            else Pair(moveFail.get() == 0, moveErrors.firstOrNull())
        }
        return try {
            val payload = JSONObject().apply {
                put("event_type", event.eventType)
                put("device_id", event.deviceId)
                put("saved_at", event.savedAt)
                put("event_data", tagsData)
            }.toString()
            onLog("  → POST webhook: $webhookUrl")
            onLog("  payload: ${JSONObject(payload).toString(2)}")
            val body = payload.toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url(webhookUrl)
                .post(body)
                .header("Content-Type", "application/json")
                .build()
            semaphore.withPermit {
                httpClient.newCall(request).execute().use { response ->
                    val respBody = response.body?.string() ?: ""
                    onLog("  resposta HTTP ${response.code}: $respBody")
                    if (response.isSuccessful) Pair(true, null)
                    else Pair(false, "HTTP ${response.code}: ${response.message}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "syncLocationInventoryEvent error", e)
            onLog("  ERRO webhook: ${e.message}")
            Pair(false, e.message)
        }
    }

    private fun buildMoveLocationXml(idcode: String, locationName: String) = """
        <msg>
            <command>MoveLocation</command>
            <terminal>SAPext</terminal>
            <data>
                <object>${escapeXml(idcode)}</object>
                <location>${escapeXml(locationName)}</location>
            </data>
        </msg>
    """.trimIndent()

    // -------------------------------------------------------------------------
    // Queries paginadas com filtro
    // -------------------------------------------------------------------------

    /**
     * Retorna uma página de objetos filtrada por campo/valor.
     * filterField: coluna SQLite ("description", "epc", "locationId", "active", "lastLocation").
     * filterValue: valor a buscar (LIKE %value%).
     * @return Par (lista de itens, total de itens filtrados).
     */
    suspend fun queryObjectsPaged(
        filterField: String,
        filterValue: String,
        page: Int,
        pageSize: Int = 100
    ): Pair<List<XtrackObjectEntity>, Int> = withContext(Dispatchers.IO) {
        val offset = page * pageSize
        val items = objectDao.queryPaged(filterField, filterValue, pageSize, offset)
        val total = objectDao.countFiltered(filterField, filterValue)
        Pair(items, total)
    }

    /**
     * Retorna uma página de localizações filtrada por nome.
     * search: texto parcial no campo name (LIKE %search%).
     * @return Par (lista de itens, total filtrado).
     */
    suspend fun searchLocationsPaged(
        search: String,
        page: Int,
        pageSize: Int = 100
    ): Pair<List<XtrackLocationEntity>, Int> = withContext(Dispatchers.IO) {
        val offset = page * pageSize
        val items = locationDao.searchPaged(search, pageSize, offset)
        val total = locationDao.countSearch(search)
        Pair(items, total)
    }

    // -------------------------------------------------------------------------
    // Download de dados do servidor
    // -------------------------------------------------------------------------

    /**
     * Busca objetos e identificações no servidor Xtrack, faz o join e salva no banco.
     *
     * Fluxo:
     *  1. GetObject  → mapa id → dados do objeto (description, idcode, locationId, …)
     *  2. GetIdentification → cada registro tem IDCODE (= EPC real) + OBJECT_ID
     *  3. Join: para cada identification, busca o objeto pelo OBJECT_ID e cria uma linha
     *  4. Um mesmo objeto pode ter múltiplos EPCs → múltiplas linhas, sem sobrescrever
     */
    suspend fun fetchAndSaveObjects(
        baseUrl: String,
        onLog: (String) -> Unit = {}
    ): Result<Int> = withContext(Dispatchers.IO) {
        runCatching {
            // ── 1. GetObject ────────────────────────────────────────────────
            onLog("Solicitando objetos ao servidor...")
            val objectsXml = postXml(baseUrl, """
                <msg>
                    <command>GetObject</command>
                    <terminal>ERP</terminal>
                </msg>
            """.trimIndent())
            val objectsById = parseObjectsMap(objectsXml)
            onLog("${objectsById.size} objetos recebidos. Solicitando identificações...")

            // ── 2. GetIdentification ─────────────────────────────────────────
            val identXml = postXml(baseUrl, """
                <msg>
                    <command>GetIdentification</command>
                    <terminal>ERP</terminal>
                </msg>
            """.trimIndent())
            val identifications = parseIdentifications(identXml)
            onLog("${identifications.size} identificações recebidas. Fazendo join...")

            // ── 3. Join ──────────────────────────────────────────────────────
            val items = mutableListOf<XtrackObjectEntity>()
            for ((epc, objectId) in identifications) {
                val obj = objectsById[objectId] ?: continue
                items.add(
                    XtrackObjectEntity(
                        epc = epc,
                        objectId = objectId,
                        idcode = obj["IDCODE"] ?: "",
                        description = obj["DESCRIPTION"] ?: "",
                        active = obj["ACTIVE"] ?: "",
                        locationId = obj["LOCATION_ID"] ?: "",
                        lastSeen = obj["LAST_SEEN"] ?: "",
                        homeLocationId = obj["HOME_LOCATION_ID"] ?: "",
                        lastModified = obj["LAST_MODIFIED"] ?: "",
                        lastLocation = obj["LAST_LOCATION"] ?: ""
                    )
                )
            }
            onLog("${items.size} registros prontos. Salvando no banco de dados...")

            // ── 4. Persistir (drop + insert garante índices limpos) ──────────
            objectDao.deleteAll()
            objectDao.insertAll(items)
            onLog("✓ ${items.size} registros salvos com sucesso.")
            items.size
        }.onFailure { e ->
            onLog("✗ Erro ao buscar objetos: ${e.message}")
            Log.e(TAG, "fetchAndSaveObjects error", e)
        }
    }

    /**
     * Busca todas as localizações no servidor Xtrack e salva no banco local.
     */
    suspend fun fetchAndSaveLocations(
        baseUrl: String,
        onLog: (String) -> Unit = {}
    ): Result<Int> = withContext(Dispatchers.IO) {
        runCatching {
            onLog("Solicitando localizações ao servidor...")
            val xml = """
                <msg>
                    <command>GetLocation</command>
                    <terminal>ERP</terminal>
                </msg>
            """.trimIndent()

            val responseXml = postXml(baseUrl, xml)
            onLog("Resposta recebida. Interpretando XML...")

            val items = parseLocations(responseXml)
            onLog("${items.size} localizações encontradas. Salvando no banco de dados...")

            locationDao.deleteAll()
            locationDao.insertAll(items)
            onLog("✓ ${items.size} localizações salvas com sucesso.")
            items.size
        }.onFailure { e ->
            onLog("✗ Erro ao buscar localizações: ${e.message}")
            Log.e(TAG, "fetchAndSaveLocations error", e)
        }
    }

    // -------------------------------------------------------------------------
    // POST genérico para operações futuras (SetObject, SetLocation, etc.)
    // -------------------------------------------------------------------------

    /**
     * Envia um payload XML personalizado ao servidor.
     * Use para comandos futuros como SetObject, SetLocation, etc.
     */
    suspend fun postXmlCommand(
        baseUrl: String,
        command: String,
        fields: Map<String, String> = emptyMap()
    ): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val xml = buildXmlCommand(command, fields)
            postXml(baseUrl, xml)
        }.onFailure { e ->
            Log.e(TAG, "postXmlCommand($command) error", e)
        }
    }

    /**
     * Envia um XML bruto ao servidor (override completo do payload).
     */
    suspend fun postRawXml(baseUrl: String, xmlBody: String): Result<String> =
        withContext(Dispatchers.IO) {
            runCatching { postXml(baseUrl, xmlBody) }
                .onFailure { e -> Log.e(TAG, "postRawXml error", e) }
        }

    // -------------------------------------------------------------------------
    // Internos — HTTP
    // -------------------------------------------------------------------------

    private fun postXml(baseUrl: String, xmlBody: String): String {
        val body = xmlBody.toRequestBody("application/xml; charset=utf-8".toMediaType())
        val request = Request.Builder()
            .url(baseUrl)
            .post(body)
            .build()
        httpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw Exception("HTTP ${response.code}: ${response.message}")
            }
            return response.body?.string()
                ?: throw Exception("Resposta vazia do servidor")
        }
    }

    private fun buildXmlCommand(command: String, fields: Map<String, String>): String {
        val sb = StringBuilder()
        sb.append("<msg>")
        sb.append("<command>$command</command>")
        sb.append("<terminal>ERP</terminal>")
        fields.forEach { (k, v) -> sb.append("<$k>${escapeXml(v)}</$k>") }
        sb.append("</msg>")
        return sb.toString()
    }

    private fun escapeXml(s: String): String = s
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&apos;")

    // -------------------------------------------------------------------------
    // Internos — parsers XML
    // -------------------------------------------------------------------------

    /**
     * Parseia a resposta do GetObject e retorna um mapa indexado pelo ID do objeto.
     * Cada valor é um mapa de campos (IDCODE, DESCRIPTION, LOCATION_ID, etc.).
     */
    private fun parseObjectsMap(xmlStr: String): Map<String, Map<String, String>> {
        val result = mutableMapOf<String, MutableMap<String, String>>()
        try {
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setInput(StringReader(xmlStr))

            var inData = false
            val fields = mutableMapOf<String, String>()
            var currentTag = ""

            var event = parser.eventType
            while (event != XmlPullParser.END_DOCUMENT) {
                when (event) {
                    XmlPullParser.START_TAG -> {
                        currentTag = parser.name
                        if (currentTag == "data") { inData = true; fields.clear() }
                    }
                    XmlPullParser.TEXT -> {
                        if (inData) fields[currentTag] = (fields[currentTag] ?: "") + (parser.text ?: "")
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "data" && inData) {
                            val id = fields["ID"]?.trim()
                            if (!id.isNullOrBlank()) {
                                result[id] = fields.mapValues { it.value.trim() }.toMutableMap()
                            }
                            inData = false
                        }
                    }
                }
                event = parser.next()
            }
        } catch (e: Exception) {
            Log.e(TAG, "parseObjectsMap error", e)
            throw Exception("Erro ao interpretar XML de objetos: ${e.message}")
        }
        return result
    }

    /**
     * Parseia a resposta do GetIdentification.
     * Retorna lista de pares (epc, objectId):
     *  - epc      = IDCODE do GetIdentification (EPC real da tag, normalizado para uppercase)
     *  - objectId = OBJECT_ID do GetIdentification (referencia o ID do GetObject)
     *
     * Registros sem EPC ou sem OBJECT_ID são ignorados.
     * Múltiplos registros com o mesmo OBJECT_ID são todos mantidos.
     */
    private fun parseIdentifications(xmlStr: String): List<Pair<String, String>> {
        val result = mutableListOf<Pair<String, String>>()
        try {
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setInput(StringReader(xmlStr))

            var inData = false
            val fields = mutableMapOf<String, String>()
            var currentTag = ""

            var event = parser.eventType
            while (event != XmlPullParser.END_DOCUMENT) {
                when (event) {
                    XmlPullParser.START_TAG -> {
                        currentTag = parser.name
                        if (currentTag == "data") { inData = true; fields.clear() }
                    }
                    XmlPullParser.TEXT -> {
                        if (inData) fields[currentTag] = (fields[currentTag] ?: "") + (parser.text ?: "")
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "data" && inData) {
                            val epc = fields["IDCODE"]?.trim()?.uppercase() ?: ""
                            val objectId = fields["OBJECT_ID"]?.trim() ?: ""
                            val type = fields["TYPE"]?.trim()?.uppercase() ?: ""
                            if (epc.isNotBlank() && objectId.isNotBlank() && type == "RFID") {
                                result.add(epc to objectId)
                            }
                            inData = false
                        }
                    }
                }
                event = parser.next()
            }
        } catch (e: Exception) {
            Log.e(TAG, "parseIdentifications error", e)
            throw Exception("Erro ao interpretar XML de identificações: ${e.message}")
        }
        return result
    }

    private fun parseLocations(xmlStr: String): List<XtrackLocationEntity> {
        val items = mutableListOf<XtrackLocationEntity>()
        try {
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setInput(StringReader(xmlStr))

            var inData = false
            val fields = mutableMapOf<String, String>()
            var currentTag = ""

            var event = parser.eventType
            while (event != XmlPullParser.END_DOCUMENT) {
                when (event) {
                    XmlPullParser.START_TAG -> {
                        currentTag = parser.name
                        if (currentTag == "data") {
                            inData = true
                            fields.clear()
                        }
                    }
                    XmlPullParser.TEXT -> {
                        if (inData) {
                            val text = parser.text ?: ""
                            fields[currentTag] = (fields[currentTag] ?: "") + text
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "data" && inData) {
                            val id = fields["ID"]?.trim() ?: ""
                            if (id.isNotBlank()) {
                                items.add(
                                    XtrackLocationEntity(
                                        id = id,
                                        name = fields["NAME"]?.trim() ?: ""
                                    )
                                )
                            }
                            inData = false
                        }
                    }
                }
                event = parser.next()
            }
        } catch (e: Exception) {
            Log.e(TAG, "parseLocations error", e)
            throw Exception("Erro ao interpretar XML de localizações: ${e.message}")
        }
        return items
    }
}
