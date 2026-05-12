package com.example.minigrocery.data.model

/**
 * Combines a Product with its quantity in the cart.
 * This is a UI model — NOT stored in Room directly.
 * Room stores CartEntity (just IDs + quantity).
 * We join them in the Repository to get this full object.
 */
data class CartItem(
    val product: Product,
    var quantity: Int
) {
    // Convenience computed property
    val totalPrice: Double get() = product.price * quantity
}