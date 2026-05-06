package com.smartx.rfidreader.ui.xtrack

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.smartx.rfidreader.R
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class XtrackConfigDialogFragment : DialogFragment() {

    private val viewModel: XtrackViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.dialog_xtrack_config, null)

        val editUrl = view.findViewById<com.google.android.material.textfield.TextInputEditText>(
            R.id.editXtrackConfigUrl
        )
        val btnExample = view.findViewById<com.google.android.material.button.MaterialButton>(
            R.id.btnXtrackConfigExample
        )
        val btnSave = view.findViewById<com.google.android.material.button.MaterialButton>(
            R.id.btnXtrackConfigSave
        )

        lifecycleScope.launch {
            val current = viewModel.uiState.first()
            editUrl.setText(current.url)
        }

        btnExample.setOnClickListener {
            editUrl.setText(getString(R.string.xtrack_example_url))
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.xtrack_config_dialog_title)
            .setView(view)
            .setNegativeButton(android.R.string.cancel, null)
            .create()

        btnSave.setOnClickListener {
            val url = editUrl.text?.toString()?.trim() ?: ""
            if (url.isNotBlank() && !url.startsWith("http")) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.error_invalid_url),
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            viewModel.saveUrl(url)
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                getString(R.string.xtrack_url_saved),
                Snackbar.LENGTH_SHORT
            ).show()
            dialog.dismiss()
        }

        return dialog
    }
}
