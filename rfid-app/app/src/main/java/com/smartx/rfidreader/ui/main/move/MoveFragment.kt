package com.smartx.rfidreader.ui.main.move

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
import com.smartx.rfidreader.databinding.FragmentMoveBinding
import com.smartx.rfidreader.ui.main.MainViewModel
import com.smartx.rfidreader.ui.main.XtrackTagInfo
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MoveFragment : Fragment() {

    private var _binding: FragmentMoveBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: MoveTagAdapter
    private var toneGenerator: ToneGenerator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setDisplayLimit(null)
        viewModel.clearTags()
        viewModel.clearMoveContext()
        setupRecyclerView()
        setupButtons()
        observeState()
        loadLocationsAndShowDialog()
    }

    private fun loadLocationsAndShowDialog() {
        viewLifecycleOwner.lifecycleScope.launch {
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
            viewModel.initMoveLocation(location.id, location.name)
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
        adapter = MoveTagAdapter()
        binding.recyclerViewMoveTags.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMoveTags.adapter = adapter
    }

    private fun setupButtons() {
        binding.btnMoveToggleInventory.setOnClickListener {
            viewModel.toggleInventory()
        }
        binding.btnMoveClearTags.setOnClickListener {
            viewModel.clearTags()
        }
        binding.btnMoveSave.setOnClickListener {
            binding.btnMoveSave.isEnabled = false
            viewModel.saveMoveLocation()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Estado de conexão e inventário
                launch {
                    viewModel.uiState.collect { state ->
                        val connected = state.connectionState == ReaderConnectionState.CONNECTED
                        binding.btnMoveToggleInventory.isEnabled = connected
                        binding.btnMoveClearTags.isEnabled = connected

                        if (state.isInventorying) {
                            binding.btnMoveToggleInventory.text = getString(R.string.btn_stop_inventory)
                            binding.btnMoveSave.isEnabled = false
                        } else {
                            binding.btnMoveToggleInventory.text = getString(R.string.btn_start_inventory)
                            val hasLocation = viewModel.moveContext.value.locationId.isNotEmpty()
                            val hasTags = viewModel.tags.value.isNotEmpty()
                            binding.btnMoveSave.isEnabled = connected && hasLocation && hasTags
                        }
                    }
                }

                // Contexto + tags lidas + infoMap → atualiza lista e contador
                launch {
                    combine(
                        viewModel.moveContext,
                        viewModel.tags,
                        viewModel.xtrackTagInfoMap
                    ) { ctx, tags, infoMap ->
                        Triple(ctx, tags, infoMap)
                    }.collect { (ctx, tags, infoMap) ->

                        binding.textMoveLocationName.text = ctx.locationName.ifBlank {
                            getString(R.string.loc_inv_select_location)
                        }
                        binding.textMoveCounter.text =
                            getString(R.string.move_counter, tags.size)

                        val items = tags.map { tag ->
                            val info = infoMap[tag.epc] ?: XtrackTagInfo()
                            MoveTagItem(
                                epc = tag.epc,
                                idcode = info.idcode,
                                description = when {
                                    tag.description == null -> "…"
                                    info.description.isBlank() -> getString(R.string.move_tag_not_found)
                                    else -> info.description
                                }
                            )
                        }

                        val notFoundLabel = getString(R.string.move_tag_not_found)
                        val notFoundCount = items.count { it.description == notFoundLabel }
                        val foundItems = items.filter { it.description != notFoundLabel }

                        adapter.submitList(foundItems)

                        if (notFoundCount > 0) {
                            binding.cardMoveNotFound.visibility = android.view.View.VISIBLE
                            binding.textMoveNotFoundCount.text = notFoundCount.toString()
                        } else {
                            binding.cardMoveNotFound.visibility = android.view.View.GONE
                        }

                        // Reabilita salvar se parado e tem tags + local
                        if (!viewModel.uiState.value.isInventorying) {
                            val connected = viewModel.uiState.value.connectionState ==
                                    ReaderConnectionState.CONNECTED
                            binding.btnMoveSave.isEnabled =
                                connected && ctx.locationId.isNotEmpty() && tags.isNotEmpty()
                        }
                    }
                }

                // Buzzer
                launch {
                    viewModel.buzzerEvent.collect { playBeep() }
                }

                // Resultado do salvamento
                launch {
                    viewModel.saveMoveResult.collect { success ->
                        val msg = if (success)
                            getString(R.string.move_saved)
                        else
                            getString(R.string.move_save_error)

                        if (success) {
                            Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
                            binding.root.postDelayed({
                                parentFragmentManager.popBackStack()
                            }, 1000)
                        } else {
                            val ctx = viewModel.moveContext.value
                            val connected = viewModel.uiState.value.connectionState ==
                                    ReaderConnectionState.CONNECTED
                            binding.btnMoveSave.isEnabled =
                                connected && ctx.locationId.isNotEmpty() && viewModel.tags.value.isNotEmpty()
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.stopInventory()
        _binding = null
    }
}
