package com.smartx.rfidreader.ui.xtrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.smartx.rfidreader.R
import com.smartx.rfidreader.databinding.BottomSheetXtrackObjectsBinding
import kotlinx.coroutines.launch

class XtrackObjectsBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetXtrackObjectsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: XtrackViewModel by activityViewModels()
    private lateinit var adapter: XtrackObjectAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetXtrackObjectsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = XtrackObjectAdapter()
        binding.recyclerObjSheet.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@XtrackObjectsBottomSheet.adapter
            isNestedScrollingEnabled = false
        }

        binding.btnObjSheetSearch.setOnClickListener {
            val epcFilter = binding.editObjSheetEpc.text?.toString()?.trim()?.uppercase() ?: ""
            if (epcFilter.isNotBlank()) {
                viewModel.applyObjectFilter("epc", epcFilter)
            } else {
                viewModel.clearObjectFilter()
            }
        }

        binding.editObjSheetEpc.setOnEditorActionListener { _, _, _ ->
            binding.btnObjSheetSearch.performClick()
            true
        }

        binding.btnObjSheetNext.setOnClickListener { viewModel.objectsNextPage() }
        binding.btnObjSheetPrev.setOnClickListener { viewModel.objectsPrevPage() }

        // Carrega sem filtro ao abrir
        viewModel.loadObjectsPage(0, "", "")

        observeObjects()
    }

    private fun observeObjects() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.objectsState.collect { state ->
                    adapter.locationNames = state.locationNames
                    adapter.submitList(state.items)

                    binding.textObjSheetCount.text = getString(
                        R.string.xtrack_objects_count, state.total
                    )

                    val hasPagination = state.totalPages > 1
                    binding.layoutObjSheetPagination.visibility =
                        if (hasPagination) View.VISIBLE else View.GONE
                    if (hasPagination) {
                        binding.textObjSheetPageInfo.text = getString(
                            R.string.xtrack_page_info,
                            state.page + 1,
                            state.totalPages,
                            state.total
                        )
                        binding.btnObjSheetPrev.isEnabled = state.page > 0
                        binding.btnObjSheetNext.isEnabled = state.page < state.totalPages - 1
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
