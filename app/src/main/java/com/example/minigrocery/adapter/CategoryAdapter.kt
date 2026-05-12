package com.example.minigrocery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.minigrocery.data.model.Category
import com.example.minigrocery.databinding.ItemCategoryBinding

/**
 * ListAdapter uses DiffUtil automatically — it only redraws
 * items that actually changed, which is more efficient than
 * notifyDataSetChanged().
 */
class CategoryAdapter(
    private val onCategoryClick: (Category) -> Unit
) : ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CategoryViewHolder(
        private val binding: ItemCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.tvCategoryEmoji.text = category.emoji
            binding.tvCategoryName.text = category.name

            // Highlight selected category with green background
            binding.cardCategory.setCardBackgroundColor(
                if (category.isSelected) {
                    binding.root.context.getColor(com.example.minigrocery.R.color.primary_green)
                } else {
                    binding.root.context.getColor(com.example.minigrocery.R.color.card_background)
                }
            )

            // Change text color when selected
            binding.tvCategoryName.setTextColor(
                if (category.isSelected) {
                    binding.root.context.getColor(com.example.minigrocery.R.color.surface_white)
                } else {
                    binding.root.context.getColor(com.example.minigrocery.R.color.text_primary)
                }
            )

            binding.cardCategory.setOnClickListener {
                onCategoryClick(category)
            }
        }
    }

    // DiffUtil tells RecyclerView exactly which items changed
    class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id  // same row?
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem  // same data? (data class handles this)
        }
    }
}