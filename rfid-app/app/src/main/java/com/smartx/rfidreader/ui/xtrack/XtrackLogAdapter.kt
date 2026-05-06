package com.smartx.rfidreader.ui.xtrack

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartx.rfidreader.databinding.ItemXtrackLogBinding

class XtrackLogAdapter : ListAdapter<String, XtrackLogAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemXtrackLogBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(line: String) {
            binding.textLogLine.text = line
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemXtrackLogBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    }
}
