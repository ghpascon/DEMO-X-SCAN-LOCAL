package com.smartx.rfidreader.ui.xtrack

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.smartx.rfidreader.R
import com.smartx.rfidreader.databinding.ActivityXtrackBinding
import kotlinx.coroutines.launch

class XtrackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityXtrackBinding
    private val viewModel: XtrackViewModel by viewModels()
    private lateinit var logAdapter: XtrackLogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityXtrackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupHeader()
        setupDownloadButton()
        setupCounterCards()
        setupLogList()
        observeState()
    }

    private fun setupHeader() {
        binding.headerApp.headerLogo.setOnClickListener { finish() }
        binding.headerApp.headerReaderName.text = getString(R.string.nav_xtrack)
        binding.headerApp.headerConnectionStatus.text = ""
        binding.headerApp.headerStatusDot.visibility = View.GONE

        binding.btnOpenConfig.setOnClickListener {
            XtrackConfigDialogFragment().show(supportFragmentManager, "xtrack_config")
        }
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
            }
        }
    }
}
