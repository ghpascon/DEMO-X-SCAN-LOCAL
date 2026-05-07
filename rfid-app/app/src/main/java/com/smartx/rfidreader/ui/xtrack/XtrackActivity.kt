package com.smartx.rfidreader.ui.xtrack

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.smartx.rfidreader.R
import com.smartx.rfidreader.core.db.XtrackEventEntity
import com.smartx.rfidreader.databinding.ActivityXtrackBinding
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class XtrackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityXtrackBinding
    private val viewModel: XtrackViewModel by viewModels()
    private lateinit var logAdapter: XtrackLogAdapter
    private lateinit var syncLogAdapter: XtrackLogAdapter
    private lateinit var eventListAdapter: XtrackEventListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityXtrackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupHeader()
        setupDownloadButton()
        setupCounterCards()
        setupLogList()
        setupEventsList()
        setupXtrackEventsSection()
        observeState()
        binding.btnOpenConfig.setOnClickListener {
            XtrackConfigDialogFragment().show(supportFragmentManager, "xtrack_config")
        }
    }

    private fun setupHeader() {
        binding.headerApp.headerLogo.setOnClickListener { finish() }
        binding.headerApp.headerReaderName.text = getString(R.string.nav_xtrack)
        binding.headerApp.headerConnectionStatus.text = ""
        binding.headerApp.headerStatusDot.visibility = View.GONE
    }

    private fun setupDownloadButton() {
        binding.btnDownloadData.setOnClickListener {
            viewModel.downloadData(onNoUrl = {
                Snackbar.make(binding.root, getString(R.string.xtrack_no_url), Snackbar.LENGTH_SHORT).show()
            })
        }
    }

    private fun setupCounterCards() {
        // Objetos → abre BottomSheet com lista + filtro EPC
        binding.cardObjectCount.setOnClickListener {
            XtrackObjectsBottomSheet().show(supportFragmentManager, "xtrack_objects")
        }
        // Localizações → abre BottomSheet com lista + busca
        binding.cardLocationCount.setOnClickListener {
            XtrackLocationsBottomSheet().show(supportFragmentManager, "xtrack_locations")
        }
    }

    private fun setupLogList() {
        logAdapter = XtrackLogAdapter()
        binding.recyclerViewLog.apply {
            layoutManager = LinearLayoutManager(this@XtrackActivity).also { it.stackFromEnd = true }
            adapter = logAdapter
            isNestedScrollingEnabled = false
        }
        binding.btnCloseLog.setOnClickListener { viewModel.clearLog() }
    }

    private fun setupEventsList() {
        eventListAdapter = XtrackEventListAdapter(
            onDelete = { event ->
                AlertDialog.Builder(this)
                    .setTitle("Deletar evento")
                    .setMessage("Remover este evento Xtrack?")
                    .setPositiveButton("Deletar") { _, _ -> viewModel.deleteXtrackEvent(event) }
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()
            },
            onItemClick = { event -> showPayloadDialog(event) }
        )
        binding.recyclerViewXtrackEvents.apply {
            layoutManager = LinearLayoutManager(this@XtrackActivity)
            adapter = eventListAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun showPayloadDialog(event: XtrackEventEntity) {
        val pretty = runCatching {
            when {
                event.tagsJson.trimStart().startsWith("[") ->
                    JSONArray(event.tagsJson).toString(2)
                else ->
                    JSONObject(event.tagsJson).toString(2)
            }
        }.getOrElse { event.tagsJson }

        val typeLabel = when (event.eventType) {
            "change_location"    -> getString(R.string.event_type_move)
            "location_inventory" -> getString(R.string.event_type_inventory)
            else -> event.eventType
        }
        AlertDialog.Builder(this)
            .setTitle("$typeLabel — ${event.locationName}")
            .setMessage(pretty)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    private fun setupXtrackEventsSection() {
        syncLogAdapter = XtrackLogAdapter()
        binding.recyclerViewXtrackSyncLog.apply {
            layoutManager = LinearLayoutManager(this@XtrackActivity).also { it.stackFromEnd = true }
            adapter = syncLogAdapter
            isNestedScrollingEnabled = false
        }
        binding.btnSyncXtrackEvents.setOnClickListener {
            viewModel.syncXtrackEvents(onNoUrl = {
                com.google.android.material.snackbar.Snackbar.make(
                    binding.root,
                    getString(R.string.xtrack_no_url),
                    com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                ).show()
            })
        }
        binding.btnClearXtrackEvents.setOnClickListener {
            viewModel.deleteAllXtrackEvents()
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Estado principal (URL, contadores, download)
                launch {
                    viewModel.uiState.collect { state ->
                        binding.textUrlSummary.text =
                            state.url.ifBlank { getString(R.string.xtrack_url_not_configured) }
                        binding.textObjectCount.text = state.objectCount.toString()
                        binding.textLocationCount.text = state.locationCount.toString()
                        binding.btnDownloadData.isEnabled = !state.isDownloading
                        binding.progressDownload.visibility =
                            if (state.isDownloading) View.VISIBLE else View.GONE
                    }
                }

                // Log de download
                launch {
                    viewModel.log.collect { lines ->
                        logAdapter.submitList(lines)
                        if (lines.isNotEmpty()) {
                            binding.recyclerViewLog.scrollToPosition(lines.size - 1)
                        }
                        binding.cardLog.visibility =
                            if (lines.isNotEmpty()) View.VISIBLE else View.GONE
                    }
                }

                // Contagem de eventos pendentes
                launch {
                    viewModel.pendingXtrackCount.collect { count ->
                        binding.textXtrackEventsPending.text =
                            getString(R.string.xtrack_events_pending, count)
                        binding.btnSyncXtrackEvents.isEnabled = count > 0
                    }
                }

                // Lista de eventos Xtrack
                launch {
                    viewModel.xtrackEvents.collect { events ->
                        eventListAdapter.submitList(events)
                    }
                }

                // Estado de sync dos eventos Xtrack
                launch {
                    viewModel.syncState.collect { state ->
                        binding.btnSyncXtrackEvents.isEnabled = !state.isRunning &&
                                (viewModel.pendingXtrackCount.value > 0)
                        binding.progressXtrackSync.visibility =
                            if (state.isRunning) View.VISIBLE else View.GONE
                        binding.cardXtrackSyncLog.visibility =
                            if (state.log.isNotEmpty()) View.VISIBLE else View.GONE
                        syncLogAdapter.submitList(state.log)
                        if (state.log.isNotEmpty()) {
                            binding.recyclerViewXtrackSyncLog.scrollToPosition(state.log.size - 1)
                        }
                    }
                }
            }
        }
    }
}
