package com.smartx.rfidreader.ui.xtrack

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartx.rfidreader.core.db.XtrackObjectEntity
import com.smartx.rfidreader.databinding.ItemXtrackObjectBinding

class XtrackObjectAdapter : ListAdapter<XtrackObjectEntity, XtrackObjectAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemXtrackObjectBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: XtrackObjectEntity) {
            binding.textObjDescription.text = item.description.ifBlank { "—" }
            binding.textObjEpc.text = item.epc
            binding.textObjLocation.text = when {
                item.lastLocation.isNotBlank() -> "Localização: ${item.lastLocation}"
                item.locationId.isNotBlank() -> "ID local: ${item.locationId}"
                else -> "—"
            }
            binding.textObjActive.text = item.active
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemXtrackObjectBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<XtrackObjectEntity>() {
        override fun areItemsTheSame(old: XtrackObjectEntity, new: XtrackObjectEntity) = old.epc == new.epc
        override fun areContentsTheSame(old: XtrackObjectEntity, new: XtrackObjectEntity) = old == new
    }
}
