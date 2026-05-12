package com.example.minigrocery.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.minigrocery.data.model.Product
import com.example.minigrocery.databinding.ItemProductBinding
import com.example.minigrocery.utils.toCurrencyString

class ProductAdapter(
    private val onAddToCart: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    // Tracks which products are already in cart (productId → quantity)
    // Updated from HomeFragment when cart changes
    private var cartProductIds: Set<Int> = emptySet()

    fun updateCartItems(productIds: Set<Int>) {
        cartProductIds = productIds
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(
        private val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {

            // Set emoji based on category
            binding.tvProductEmoji.text = getEmojiForCategory(product.categoryId)
            binding.tvProductName.text = product.name
            binding.tvProductUnit.text = product.unit
            binding.tvPrice.text = product.price.toCurrencyString()

            // Show discount badge if applicable
            if (product.discountPercent > 0) {
                binding.tvDiscount.text = "${product.discountPercent}% OFF"
                binding.tvDiscount.visibility = android.view.View.VISIBLE
            } else {
                binding.tvDiscount.visibility = android.view.View.GONE
            }

            // Show strikethrough original price if different from current
            if (product.originalPrice > product.price) {
                binding.tvOriginalPrice.text = product.originalPrice.toCurrencyString()
                binding.tvOriginalPrice.visibility = android.view.View.VISIBLE
                // paintFlags adds the strikethrough line
                binding.tvOriginalPrice.paintFlags =
                    binding.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.tvOriginalPrice.visibility = android.view.View.GONE
            }

            // Change button if already in cart
            val isInCart = product.id in cartProductIds
            if (isInCart) {
                binding.btnAddToCart.text = "✓ ADDED"
                binding.btnAddToCart.setTextColor(
                    binding.root.context.getColor(com.example.minigrocery.R.color.surface_white)
                )
                binding.btnAddToCart.backgroundTintList =
                    android.content.res.ColorStateList.valueOf(
                        binding.root.context.getColor(com.example.minigrocery.R.color.primary_green)
                    )
            } else {
                binding.btnAddToCart.text = "ADD"
                binding.btnAddToCart.setTextColor(
                    binding.root.context.getColor(com.example.minigrocery.R.color.primary_green)
                )
                binding.btnAddToCart.backgroundTintList =
                    android.content.res.ColorStateList.valueOf(
                        binding.root.context.getColor(com.example.minigrocery.R.color.surface_white)
                    )
            }

            binding.btnAddToCart.setOnClickListener {
                onAddToCart(product)
            }
        }

        // Maps categoryId to a display emoji
        private fun getEmojiForCategory(categoryId: Int): String {
            return when (categoryId) {
                2 -> "🍎"  // Fruits
                3 -> "🥦"  // Vegetables
                4 -> "🥛"  // Dairy
                5 -> "🍿"  // Snacks
                6 -> "🧃"  // Beverages
                else -> "🛒"
            }
        }
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}