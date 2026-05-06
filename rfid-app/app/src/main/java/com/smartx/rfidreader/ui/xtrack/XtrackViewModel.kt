package com.smartx.rfidreader.ui.xtrack

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.smartx.rfidreader.RfidApplication
import com.smartx.rfidreader.core.db.XtrackLocationEntity
import com.smartx.rfidreader.core.db.XtrackObjectEntity
import com.smartx.rfidreader.core.settings.AppSettingsRepository
import com.smartx.rfidreader.core.xtrack.XtrackRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.math.ceil

private const val PAGE_SIZE = 500

data class XtrackUiState(
    val url: String = "",
    val objectCount: Int = 0,
    val locationCount: Int = 0,
    val isDownloading: Boolean = false
)

data class ObjectsState(
    val items: List<XtrackObjectEntity> = emptyList(),
    val page: Int = 0,
    val totalPages: Int = 0,
    val total: Int = 0,
    val isLoading: Boolean = false,
    val filterField: String = "",
    val filterValue: String = ""
)

data class LocationsState(
    val items: List<XtrackLocationEntity> = emptyList(),
    val page: Int = 0,
    val totalPages: Int = 0,
    val total: Int = 0,
    val isLoading: Boolean = false,
    val search: String = ""
)

class XtrackViewModel(app: Application) : AndroidViewModel(app) {

    private val rfidApp = app as RfidApplication
    private val xtrackRepo: XtrackRepository = rfidApp.xtrackRepository
    private val settingsRepo: AppSettingsRepository = rfidApp.settingsRepository

    private val _uiState = MutableStateFlow(XtrackUiState())
    val uiState: StateFlow<XtrackUiState> = _uiState.asStateFlow()

    private val _log = MutableStateFlow<List<String>>(emptyList())
    val log: StateFlow<List<String>> = _log.asStateFlow()

    private val _objectsState = MutableStateFlow(ObjectsState())
    val objectsState: StateFlow<ObjectsState> = _objectsState.asStateFlow()

    private val _locationsState = MutableStateFlow(LocationsState())
    val locationsState: StateFlow<LocationsState> = _locationsState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepo.flow.collect { settings ->
                _uiState.update { it.copy(url = settings.xtrackUrl) }
            }
        }
        refreshCounts()
        loadObjectsPage(0)
        loadLocationsPage(0)
    }

    fun saveUrl(url: String) {
        viewModelScope.launch {
            val current = settingsRepo.flow.first()
            settingsRepo.save(current.copy(xtrackUrl = url.trim()))
            _uiState.update { it.copy(url = url.trim()) }
        }
    }

    // ─── Download ────────────────────────────────────────────────────────────

    fun downloadData(onNoUrl: () -> Unit) {
        if (_uiState.value.isDownloading) return
        val url = _uiState.value.url
        if (url.isBlank()) { onNoUrl(); return }

        viewModelScope.launch {
            _uiState.update { it.copy(isDownloading = true) }
            _log.value = emptyList()
            appendLog("=== Iniciando download de dados Xtrack ===")
            appendLog("URL: $url")
            appendLog("")

            val objResult = xtrackRepo.fetchAndSaveObjects(url) { msg -> appendLog(msg) }
            appendLog("")
            val locResult = xtrackRepo.fetchAndSaveLocations(url) { msg -> appendLog(msg) }
            appendLog("")

            if (objResult.isSuccess && locResult.isSuccess) {
                appendLog("=== Download concluído com sucesso! ===")
            } else {
                appendLog("=== Download finalizado com erros. Verifique a URL e a conexão. ===")
            }

            refreshCounts()
            // Recarrega listas após download
            loadObjectsPage(0, _objectsState.value.filterField, _objectsState.value.filterValue)
            loadLocationsPage(0, _locationsState.value.search)
            _uiState.update { it.copy(isDownloading = false) }
        }
    }

    // ─── Objetos ─────────────────────────────────────────────────────────────

    fun loadObjectsPage(
        page: Int,
        filterField: String = _objectsState.value.filterField,
        filterValue: String = _objectsState.value.filterValue
    ) {
        viewModelScope.launch {
            _objectsState.update { it.copy(isLoading = true) }
            val (items, total) = xtrackRepo.queryObjectsPaged(filterField, filterValue, page, PAGE_SIZE)
            val totalPages = if (total == 0) 0 else ceil(total.toDouble() / PAGE_SIZE).toInt()
            _objectsState.update {
                it.copy(
                    items = items,
                    page = page,
                    totalPages = totalPages,
                    total = total,
                    isLoading = false,
                    filterField = filterField,
                    filterValue = filterValue
                )
            }
        }
    }

    fun applyObjectFilter(filterField: String, filterValue: String) =
        loadObjectsPage(0, filterField, filterValue)

    fun clearObjectFilter() = loadObjectsPage(0, "", "")

    fun objectsNextPage() {
        val s = _objectsState.value
        if (s.page < s.totalPages - 1) loadObjectsPage(s.page + 1)
    }

    fun objectsPrevPage() {
        val s = _objectsState.value
        if (s.page > 0) loadObjectsPage(s.page - 1)
    }

    // ─── Localizações ────────────────────────────────────────────────────────

    fun loadLocationsPage(page: Int, search: String = _locationsState.value.search) {
        viewModelScope.launch {
            _locationsState.update { it.copy(isLoading = true) }
            val (items, total) = xtrackRepo.searchLocationsPaged(search, page, PAGE_SIZE)
            val totalPages = if (total == 0) 0 else ceil(total.toDouble() / PAGE_SIZE).toInt()
            _locationsState.update {
                it.copy(
                    items = items,
                    page = page,
                    totalPages = totalPages,
                    total = total,
                    isLoading = false,
                    search = search
                )
            }
        }
    }

    fun applyLocationSearch(search: String) = loadLocationsPage(0, search)

    fun locationsNextPage() {
        val s = _locationsState.value
        if (s.page < s.totalPages - 1) loadLocationsPage(s.page + 1)
    }

    fun locationsPrevPage() {
        val s = _locationsState.value
        if (s.page > 0) loadLocationsPage(s.page - 1)
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    fun clearLog() { _log.value = emptyList() }

    private fun refreshCounts() {
        viewModelScope.launch {
            val objs = xtrackRepo.objectCount()
            val locs = xtrackRepo.locationCount()
            _uiState.update { it.copy(objectCount = objs, locationCount = locs) }
        }
    }

    private fun appendLog(msg: String) {
        _log.update { it + msg }
    }
}

