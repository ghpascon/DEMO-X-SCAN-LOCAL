package com.smartx.rfidreader.ui.main.locationinventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartx.rfidreader.core.db.XtrackObjectEntity
import com.smartx.rfidreader.databinding.ItemLocationInventoryTagBinding

class LocationInventoryTagAdapter :
    ListAdapter<XtrackObjectEntity, LocationInventoryTagAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemLocationInventoryTagBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: XtrackObjectEntity) {
            binding.textLocInvDescription.text = item.description.ifBlank { "—" }
            binding.textLocInvEpc.text = item.epc
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLocationInventoryTagBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<XtrackObjectEntity>() {
        override fun areItemsTheSame(a: XtrackObjectEntity, b: XtrackObjectEntity) = a.epc == b.epc
        override fun areContentsTheSame(a: XtrackObjectEntity, b: XtrackObjectEntity) = a == b
    }
}
