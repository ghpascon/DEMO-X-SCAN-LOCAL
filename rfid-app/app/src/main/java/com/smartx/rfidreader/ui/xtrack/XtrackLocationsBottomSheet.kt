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
import com.smartx.rfidreader.databinding.BottomSheetXtrackLocationsBinding
import kotlinx.coroutines.launch

class XtrackLocationsBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetXtrackLocationsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: XtrackViewModel by activityViewModels()
    private lateinit var adapter: XtrackLocationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetXtrackLocationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = XtrackLocationAdapter()
        binding.recyclerLocSheet.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@XtrackLocationsBottomSheet.adapter
            isNestedScrollingEnabled = false
        }

        binding.btnLocSheetSearch.setOnClickListener {
            val search = binding.editLocSheetSearch.text?.toString()?.trim() ?: ""
            viewModel.applyLocationSearch(search)
        }

        binding.editLocSheetSearch.setOnEditorActionListener { _, _, _ ->
            binding.btnLocSheetSearch.performClick()
            true
        }

        binding.btnLocSheetNext.setOnClickListener { viewModel.locationsNextPage() }
        binding.btnLocSheetPrev.setOnClickListener { viewModel.locationsPrevPage() }

        // Reload first page when sheet opens
        viewModel.loadLocationsPage(0, viewModel.locationsState.value.search)

        observeLocations()
    }

    private fun observeLocations() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.locationsState.collect { state ->
                    adapter.submitList(state.items)

                    binding.textLocSheetCount.text = getString(
                        R.string.xtrack_locations_count, state.total
                    )

                    val hasPagination = state.totalPages > 1
                    binding.layoutLocSheetPagination.visibility =
                        if (hasPagination) View.VISIBLE else View.GONE
                    if (hasPagination) {
                        binding.textLocSheetPageInfo.text = getString(
                            R.string.xtrack_page_info,
                            state.page + 1,
                            state.totalPages,
                            state.total
                        )
                        binding.btnLocSheetPrev.isEnabled = state.page > 0
                        binding.btnLocSheetNext.isEnabled = state.page < state.totalPages - 1
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
