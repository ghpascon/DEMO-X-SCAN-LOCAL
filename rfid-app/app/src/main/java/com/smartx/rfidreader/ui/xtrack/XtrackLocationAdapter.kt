package com.smartx.rfidreader.ui.xtrack

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartx.rfidreader.core.db.XtrackLocationEntity
import com.smartx.rfidreader.databinding.ItemXtrackLocationBinding

class XtrackLocationAdapter : ListAdapter<XtrackLocationEntity, XtrackLocationAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemXtrackLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: XtrackLocationEntity) {
            binding.textLocName.text = item.name.ifBlank { "—" }
            binding.textLocId.text = "ID: ${item.id}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemXtrackLocationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<XtrackLocationEntity>() {
        override fun areItemsTheSame(old: XtrackLocationEntity, new: XtrackLocationEntity) = old.id == new.id
        override fun areContentsTheSame(old: XtrackLocationEntity, new: XtrackLocationEntity) = old == new
    }
}
