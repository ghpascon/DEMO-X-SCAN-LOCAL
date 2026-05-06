package com.smartx.rfidreader.ui.main.reading

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartx.rfidreader.core.reader.RfidTag
import com.smartx.rfidreader.databinding.ItemTagBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TagListAdapter : ListAdapter<RfidTag, TagListAdapter.ViewHolder>(DiffCallback()) {

    private val timeFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())

    inner class ViewHolder(private val binding: ItemTagBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tag: RfidTag) {
            val ctx = binding.root.context

            // Descrição — elemento principal
            when (tag.description) {
                null -> {
                    // Ainda buscando no banco
                    binding.textDescription.text = "Buscando descrição…"
                    binding.textDescription.setTextColor(
                        ContextCompat.getColor(ctx, android.R.color.darker_gray)
                    )
                    binding.textDescription.alpha = 0.6f
                    binding.textDescription.setTypeface(null, android.graphics.Typeface.ITALIC)
                }
                "" -> {
                    // Não encontrado no Xtrack
                    binding.textDescription.text = "NÃO ENCONTRADO"
                    val errorColor = ctx.obtainStyledAttributes(
                        intArrayOf(com.google.android.material.R.attr.colorError)
                    ).use { it.getColor(0, android.graphics.Color.RED) }
                    binding.textDescription.setTextColor(errorColor)
                    binding.textDescription.alpha = 0.8f
                    binding.textDescription.setTypeface(null, android.graphics.Typeface.NORMAL)
                }
                else -> {
                    binding.textDescription.text = tag.description
                    val primaryColor = ctx.obtainStyledAttributes(
                        intArrayOf(android.R.attr.textColorPrimary)
                    ).use { it.getColor(0, android.graphics.Color.BLACK) }
                    binding.textDescription.setTextColor(primaryColor)
                    binding.textDescription.alpha = 1f
                    binding.textDescription.setTypeface(null, android.graphics.Typeface.BOLD)
                }
            }

            // EPC — secundário
            binding.textEpc.text = tag.epc
            binding.textRssi.text = if (tag.rssi.isNotEmpty()) "RSSI: ${tag.rssi} dBm" else ""
            binding.textCount.text = "×${tag.readCount}"
            binding.textTime.text = timeFormat.format(tag.timestamp)

            // TID — oculto (conforme solicitado)
            binding.textTid.visibility = android.view.View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<RfidTag>() {
        override fun areItemsTheSame(old: RfidTag, new: RfidTag) = old.epc == new.epc
        override fun areContentsTheSame(old: RfidTag, new: RfidTag) =
            old.readCount == new.readCount &&
            old.rssi == new.rssi &&
            old.description == new.description
    }
}
