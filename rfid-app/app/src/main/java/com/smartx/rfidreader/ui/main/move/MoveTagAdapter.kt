package com.smartx.rfidreader.ui.main.move

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartx.rfidreader.databinding.ItemMoveTagBinding

data class MoveTagItem(
    val epc: String,
    val idcode: String,
    val description: String
)

class MoveTagAdapter :
    ListAdapter<MoveTagItem, MoveTagAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemMoveTagBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MoveTagItem) {
            binding.textMoveDescription.text = item.description.ifBlank { "Não encontrado" }
            binding.textMoveIdcode.text = item.idcode.ifBlank { "—" }
            binding.textMoveEpc.text = item.epc
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMoveTagBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<MoveTagItem>() {
        override fun areItemsTheSame(a: MoveTagItem, b: MoveTagItem) = a.epc == b.epc
        override fun areContentsTheSame(a: MoveTagItem, b: MoveTagItem) = a == b
    }
}
