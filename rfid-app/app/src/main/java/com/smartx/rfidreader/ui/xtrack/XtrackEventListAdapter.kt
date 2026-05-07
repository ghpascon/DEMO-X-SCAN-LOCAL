package com.smartx.rfidreader.ui.xtrack

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartx.rfidreader.R
import com.smartx.rfidreader.core.db.XtrackEventEntity
import com.smartx.rfidreader.databinding.ItemXtrackEventBinding
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

class XtrackEventListAdapter(
    private val onDelete: (XtrackEventEntity) -> Unit,
    private val onItemClick: (XtrackEventEntity) -> Unit = {}
) : ListAdapter<XtrackEventEntity, XtrackEventListAdapter.ViewHolder>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<XtrackEventEntity>() {
            override fun areItemsTheSame(a: XtrackEventEntity, b: XtrackEventEntity) = a.id == b.id
            override fun areContentsTheSame(a: XtrackEventEntity, b: XtrackEventEntity) = a == b
        }
    }

    inner class ViewHolder(private val binding: ItemXtrackEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: XtrackEventEntity) {
            val ctx = binding.root.context

            binding.chipXtrackEventType.text = when (event.eventType) {
                "change_location"    -> ctx.getString(R.string.event_type_move)
                "location_inventory" -> ctx.getString(R.string.event_type_inventory)
                else -> event.eventType
            }

            binding.textXtrackEventLocation.text = event.locationName

            val date = formatTimestamp(event.savedAt)
            binding.textXtrackEventMeta.text = "$date · ${event.tagCount} tag(s)"

            if (event.isSynced) {
                binding.chipXtrackStatus.text = ctx.getString(R.string.event_synced)
                binding.chipXtrackStatus.setChipBackgroundColorResource(R.color.status_synced_bg)
                binding.chipXtrackStatus.setTextColor(ctx.getColor(R.color.status_synced_text))
            } else {
                binding.chipXtrackStatus.text = ctx.getString(R.string.event_pending)
                binding.chipXtrackStatus.setChipBackgroundColorResource(R.color.status_pending_bg)
                binding.chipXtrackStatus.setTextColor(ctx.getColor(R.color.status_pending_text))
            }

            binding.btnDeleteXtrackEvent.setOnClickListener { onDelete(event) }
            binding.root.setOnClickListener { onItemClick(event) }
        }

        private fun formatTimestamp(iso: String): String {
            return try {
                val zdt = ZonedDateTime.parse(iso)
                val fmt = DateTimeFormatter
                    .ofLocalizedDateTime(FormatStyle.SHORT)
                    .withLocale(Locale("pt", "BR"))
                zdt.format(fmt)
            } catch (_: Exception) {
                iso
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemXtrackEventBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
