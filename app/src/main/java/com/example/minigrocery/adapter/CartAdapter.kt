package com.example.minigrocery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.minigrocery.data.model.CartItem
import com.example.minigrocery.databinding.ItemCartBinding
import com.example.minigrocery.utils.toCurrencyString

class CartAdapter(
    private val onIncrease: (Int) -> Unit,   // productId
    private val onDecrease: (Int) -> Unit,   // productId
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CartViewHolder(
        private val binding: ItemCartBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: CartItem) {
            val product = cartItem.product

            binding.tvCartProductName.text = product.name
            binding.tvCartUnit.text = product.unit
            binding.tvQuantity.text = cartItem.quantity.toString()
            binding.tvCartItemTotal.text = cartItem.totalPrice.toCurrencyString()

            // Emoji based on category
            binding.tvCartEmoji.text = when (product.categoryId) {
                2 -> "🍎"
                3 -> "🥦"
                4 -> "🥛"
                5 -> "🍿"
                6 -> "🧃"
                else -> "🛒"
            }

            binding.btnIncrease.setOnClickListener {
                onIncrease(product.id)
            }

            binding.btnDecrease.setOnClickListener {
                onDecrease(product.id)
            }
        }
    }

    class CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }
}