package com.example.minigrocery.data.repository

import com.example.minigrocery.data.local.CartDao
import com.example.minigrocery.data.local.CartEntity
import com.example.minigrocery.data.model.CartItem
import com.example.minigrocery.data.model.Product
import com.example.minigrocery.utils.SampleData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * CartRepository is the single source of truth for cart data.
 * The ViewModel talks to this — never directly to the DAO.
 *
 * It combines:
 *   - CartEntity (from Room) — has productId + quantity
 *   - Product (from SampleData) — has name, price, image etc.
 * …to produce CartItem objects the UI can display.
 */
class CartRepository(private val cartDao: CartDao) {

    // Live stream of cart items — automatically updates UI on any DB change
    val cartItems: Flow<List<CartItem>> = cartDao.getAllCartItems().map { entities ->
        entities.mapNotNull { entity ->
            // Find the matching Product from our mock data
            val product = SampleData.getProducts().find { it.id == entity.productId }
            product?.let { CartItem(product = it, quantity = entity.quantity) }
        }
    }

    // Live stream of total item count — used for cart badge
    val totalItemCount: Flow<Int> = cartDao.getTotalItemCount().map { it ?: 0 }

    // Add product to cart or increase its quantity
    suspend fun addToCart(product: Product) {
        val existing = cartDao.getCartItem(product.id)
        if (existing != null) {
            // Already in cart — bump quantity
            cartDao.insertOrUpdate(existing.copy(quantity = existing.quantity + 1))
        } else {
            // New item
            cartDao.insertOrUpdate(CartEntity(productId = product.id, quantity = 1))
        }
    }

    // Increase quantity by 1
    suspend fun increaseQuantity(productId: Int) {
        val existing = cartDao.getCartItem(productId) ?: return
        cartDao.insertOrUpdate(existing.copy(quantity = existing.quantity + 1))
    }

    // Decrease quantity — removes item if quantity hits 0
    suspend fun decreaseQuantity(productId: Int) {
        val existing = cartDao.getCartItem(productId) ?: return
        if (existing.quantity <= 1) {
            cartDao.deleteItem(existing)
        } else {
            cartDao.insertOrUpdate(existing.copy(quantity = existing.quantity - 1))
        }
    }

    // Remove item entirely
    suspend fun removeFromCart(productId: Int) {
        val existing = cartDao.getCartItem(productId) ?: return
        cartDao.deleteItem(existing)
    }

    // Wipe cart after order is placed
    suspend fun clearCart() {
        cartDao.clearCart()
    }
}