package com.smartx.rfidreader.ui.main.locationinventory

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.smartx.rfidreader.R
import com.smartx.rfidreader.core.db.XtrackLocationEntity
import com.smartx.rfidreader.core.reader.ReaderConnectionState
import com.smartx.rfidreader.databinding.DialogSelectLocationBinding
import com.smartx.rfidreader.databinding.FragmentLocationInventoryBinding
import com.smartx.rfidreader.ui.main.MainViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class LocationInventoryFragment : Fragment() {

    private var _binding: FragmentLocationInventoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: LocationInventoryTagAdapter
    private var toneGenerator: ToneGenerator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationInventoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setDisplayLimit(50)
        viewModel.clearTags()
        viewModel.clearLocationInventory()
        setupRecyclerView()
        setupButtons()
        observeState()
        loadLocationsAndShowDialog()
    }

    private fun loadLocationsAndShowDialog() {
        viewLifecycleOwner.lifecycleScope.launch {
            binding.progressLocationInventory.visibility = View.VISIBLE
            viewModel.syncBeforeLocationInventory()
            binding.progressLocationInventory.visibility = View.GONE

            val locations = viewModel.getAllLocations()
            if (locations.isEmpty()) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.loc_inv_no_locations),
                    Snackbar.LENGTH_LONG
                ).show()
                parentFragmentManager.popBackStack()
                return@launch
            }
            showLocationSelectionDialog(locations)
        }
    }

    private fun showLocationSelectionDialog(locations: List<XtrackLocationEntity>) {
        val dialogBinding = DialogSelectLocationBinding.inflate(layoutInflater)
        val showList = ArrayList(locations)

        val listAdapter = object : ArrayAdapter<XtrackLocationEntity>(
            requireContext(), android.R.layout.simple_list_item_1, showList
        ) {
            override fun getView(pos: Int, cv: View?, parent: ViewGroup): View {
                val v = cv ?: LayoutInflater.from(context)
                    .inflate(android.R.layout.simple_list_item_1, parent, false)
                (v as TextView).text = getItem(pos)?.name ?: ""
                return v
            }
        }
        dialogBinding.listViewLocations.adapter = listAdapter

        var dialog: androidx.appcompat.app.AlertDialog? = null

        dialogBinding.editSearchLocation.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val q = s?.toString()?.lowercase() ?: ""
                listAdapter.clear()
                listAdapter.addAll(
                    if (q.isBlank()) locations
                    else locations.filter { it.name.lowercase().contains(q) }
                )
                listAdapter.notifyDataSetChanged()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        dialogBinding.listViewLocations.setOnItemClickListener { _, _, pos, _ ->
            val location = listAdapter.getItem(pos) ?: return@setOnItemClickListener
            dialog?.dismiss()
            viewModel.initLocationInventory(location.id, location.name)
        }

        dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.loc_inv_select_location))
            .setView(dialogBinding.root)
            .setNegativeButton(getString(R.string.btn_cancel)) { _, _ ->
                parentFragmentManager.popBackStack()
            }
            .setOnCancelListener {
                parentFragmentManager.popBackStack()
            }
            .create()

        dialog.show()
    }

    private fun setupRecyclerView() {
        adapter = LocationInventoryTagAdapter()
        binding.recyclerViewRemainingTags.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewRemainingTags.adapter = adapter
    }

    private fun setupButtons() {
        binding.btnToggleInventory.setOnClickListener {
            viewModel.toggleInventory()
        }
        binding.btnClearTags.setOnClickListener {
            viewModel.clearTags()
        }
        binding.btnSaveReading.setOnClickListener {
            binding.btnSaveReading.isEnabled = false
            viewModel.saveLocationInventory()
        }
        binding.chipGroupLimit.setOnCheckedStateChangeListener { _, checkedIds ->
            val limit: Int? = when (checkedIds.firstOrNull()) {
                R.id.chip50  -> 50
                R.id.chip100 -> 100
                R.id.chip200 -> 200
                else         -> null
            }
            viewModel.setDisplayLimit(limit)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Estado de conexão e inventário
                launch {
                    viewModel.uiState.collect { state ->
                        val connected = state.connectionState == ReaderConnectionState.CONNECTED
                        binding.btnToggleInventory.isEnabled = connected
                        binding.btnClearTags.isEnabled = connected

                        if (state.isInventorying) {
                            binding.btnToggleInventory.text = getString(R.string.btn_stop_inventory)
                            binding.btnSaveReading.isEnabled = false
                        } else {
                            binding.btnToggleInventory.text = getString(R.string.btn_start_inventory)
                            val hasLocation =
                                viewModel.locationInventoryContext.value.expectedTags.isNotEmpty()
                            binding.btnSaveReading.isEnabled = connected && hasLocation
                        }
                    }
                }

                // Contexto + tags lidas + limite → atualiza contador e lista de restantes
                launch {
                    combine(
                        viewModel.locationInventoryContext,
                        viewModel.tags,
                        viewModel.displayLimit
                    ) { ctx, scannedTags, limit ->
                        Triple(ctx, scannedTags, limit)
                    }.collect { (ctx, scannedTags, limit) ->
                        val total = ctx.expectedTags.size
                        val scannedEpcs = scannedTags.map { it.epc.uppercase() }.toSet()
                        val allFoundEpcs = scannedEpcs + ctx.preFoundEpcs.map { it.uppercase() }
                        val remaining =
                            ctx.expectedTags.filter { it.epc.uppercase() !in allFoundEpcs }
                        val found = total - remaining.size

                        binding.textLocationName.text = ctx.locationName.ifBlank {
                            getString(R.string.loc_inv_select_location)
                        }
                        binding.textInventoryCounter.text =
                            getString(R.string.loc_inv_counter, found, total)

                        val visible = if (limit == null) remaining else remaining.take(limit)
                        adapter.submitList(visible)

                        // Reabilita salvar se parou o inventário e tem local selecionado
                        if (!viewModel.uiState.value.isInventorying) {
                            val connected = viewModel.uiState.value.connectionState ==
                                    ReaderConnectionState.CONNECTED
                            binding.btnSaveReading.isEnabled = total > 0 && connected
                        }
                    }
                }

                // Buzzer
                launch {
                    viewModel.buzzerEvent.collect { playBeep() }
                }

                // Resultado do salvamento
                launch {
                    viewModel.saveLocationInventoryResult.collect { success ->
                        val msg = if (success)
                            getString(R.string.reading_saved)
                        else
                            getString(R.string.reading_save_error)

                        if (success) {
                            Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
                            binding.root.postDelayed({
                                parentFragmentManager.popBackStack()
                            }, 1000)
                        } else {
                            val hasLocation =
                                viewModel.locationInventoryContext.value.expectedTags.isNotEmpty()
                            val connected = viewModel.uiState.value.connectionState ==
                                    ReaderConnectionState.CONNECTED
                            binding.btnSaveReading.isEnabled = hasLocation && connected
                            Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun playBeep() {
        toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 100)
    }

    override fun onStart() {
        super.onStart()
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME)
        } catch (_: Exception) {}
    }

    override fun onStop() {
        super.onStop()
        toneGenerator?.release()
        toneGenerator = null
        viewModel.stopInventory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
