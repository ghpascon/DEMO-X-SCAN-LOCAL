package com.smartx.rfidreader.ui.main.reading

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.R as MaterialR
import com.smartx.rfidreader.databinding.ItemTagGroupBinding

enum class TagGroupState { LOADING, NOT_FOUND, FOUND }

data class TagGroup(
    val displayLabel: String,
    val count: Int,
    val state: TagGroupState
)

class TagGroupAdapter : ListAdapter<TagGroup, TagGroupAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemTagGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(group: TagGroup) {
            val ctx = binding.root.context

            binding.textGroupCount.text = "×${group.count}"

            when (group.state) {
                TagGroupState.LOADING -> {
                    binding.textGroupDescription.text = group.displayLabel
                    binding.textGroupDescription.setTextColor(
                        ContextCompat.getColor(ctx, android.R.color.darker_gray)
                    )
                    binding.textGroupDescription.alpha = 0.6f
                    binding.textGroupDescription.setTypeface(null, Typeface.ITALIC)
                    binding.textGroupCount.alpha = 0.6f
                }
                TagGroupState.NOT_FOUND -> {
                    binding.textGroupDescription.text = group.displayLabel
                    val errorColor = ctx.obtainStyledAttributes(
                        intArrayOf(MaterialR.attr.colorError)
                    ).use { it.getColor(0, android.graphics.Color.RED) }
                    binding.textGroupDescription.setTextColor(errorColor)
                    binding.textGroupDescription.alpha = 0.85f
                    binding.textGroupDescription.setTypeface(null, Typeface.NORMAL)
                    binding.textGroupCount.setTextColor(errorColor)
                    binding.textGroupCount.alpha = 0.85f
                }
                TagGroupState.FOUND -> {
                    binding.textGroupDescription.text = group.displayLabel
                    val primaryColor = ctx.obtainStyledAttributes(
                        intArrayOf(android.R.attr.textColorPrimary)
                    ).use { it.getColor(0, android.graphics.Color.BLACK) }
                    binding.textGroupDescription.setTextColor(primaryColor)
                    binding.textGroupDescription.alpha = 1f
                    binding.textGroupDescription.setTypeface(null, Typeface.BOLD)
                    val accentColor = ctx.obtainStyledAttributes(
                        intArrayOf(com.google.android.material.R.attr.colorPrimary)
                    ).use { it.getColor(0, android.graphics.Color.BLUE) }
                    binding.textGroupCount.setTextColor(accentColor)
                    binding.textGroupCount.alpha = 1f
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTagGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<TagGroup>() {
        override fun areItemsTheSame(a: TagGroup, b: TagGroup) = a.displayLabel == b.displayLabel
        override fun areContentsTheSame(a: TagGroup, b: TagGroup) = a == b
    }
}

/** Converte lista de tags individuais para grupos agregados por descrição. */
fun groupTags(tags: List<com.smartx.rfidreader.core.reader.RfidTag>): List<TagGroup> {
    // Acumula contagem por chave de exibição
    data class Acc(val state: TagGroupState, var count: Int)
    val map = linkedMapOf<String, Acc>()

    for (tag in tags) {
        val (key, state) = when (tag.description) {
            null -> "Buscando descrição…" to TagGroupState.LOADING
            ""   -> "NÃO ENCONTRADO" to TagGroupState.NOT_FOUND
            else -> tag.description to TagGroupState.FOUND
        }
        val existing = map[key]
        if (existing == null) map[key] = Acc(state, 1)
        else existing.count++
    }

    // Ordena: FOUND alfabético → NÃO ENCONTRADO → LOADING
    return map.entries
        .map { (key, acc) -> TagGroup(key, acc.count, acc.state) }
        .sortedWith(
            compareBy(
                { when (it.state) { TagGroupState.FOUND -> 0; TagGroupState.NOT_FOUND -> 1; TagGroupState.LOADING -> 2 } },
                { it.displayLabel }
            )
        )
}
