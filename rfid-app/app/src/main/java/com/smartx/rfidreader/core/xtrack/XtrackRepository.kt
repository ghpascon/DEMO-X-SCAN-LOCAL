package com.smartx.rfidreader.core.xtrack

import android.util.Log
import android.util.Xml
import com.smartx.rfidreader.core.db.XtrackLocationDao
import com.smartx.rfidreader.core.db.XtrackLocationEntity
import com.smartx.rfidreader.core.db.XtrackObjectDao
import com.smartx.rfidreader.core.db.XtrackObjectEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.xmlpull.v1.XmlPullParser
import java.io.StringReader
import java.util.concurrent.TimeUnit

/**
 * Responsável por toda a integração com o servidor Xtrack:
 *  - Requisições HTTP (GET/POST com XML)
 *  - Persistência local de objetos e localizações
 *  - Lookup rápido por EPC (usado em background durante leitura)
 */
class XtrackRepository(
    private val objectDao: XtrackObjectDao,
    private val locationDao: XtrackLocationDao
) {

    private val TAG = "XtrackRepository"

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // -------------------------------------------------------------------------
    // Flows para observação reativa
    // -------------------------------------------------------------------------

    val objectsFlow: Flow<List<XtrackObjectEntity>> = objectDao.allFlow()
    val locationsFlow: Flow<List<XtrackLocationEntity>> = locationDao.allFlow()

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
