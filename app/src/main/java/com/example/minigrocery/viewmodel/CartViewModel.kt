package com.example.minigrocery.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.minigrocery.data.local.AppDatabase
import com.example.minigrocery.data.model.CartItem
import com.example.minigrocery.data.model.Product
import com.example.minigrocery.data.repository.CartRepository
import com.example.minigrocery.utils.Constants
import kotlinx.coroutines.launch

/**
 * AndroidViewModel (not plain ViewModel) because it needs
 * Application context to build the Room database instance.
 */
class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CartRepository

    val cartItems: LiveData<List<CartItem>>
    val totalItemCount: LiveData<Int>

    init {
        val dao = AppDatabase.getDatabase(application).cartDao()
        repository = CartRepository(dao)
        cartItems = repository.cartItems.asLiveData()
        totalItemCount = repository.totalItemCount.asLiveData()
    }

    // Computed bill values — updated whenever cartItems changes
    fun getSubtotal(items: List<CartItem>): Double =
        items.sumOf { it.totalPrice }

    fun getDeliveryFee(subtotal: Double): Double =
        if (subtotal >= Constants.FREE_DELIVERY_THRESHOLD) 0.0
        else Constants.DELIVERY_FEE

    fun getTotal(items: List<CartItem>): Double {
        val subtotal = getSubtotal(items)
        return subtotal + getDeliveryFee(subtotal)
    }

    // Cart actions — all run on background thread via viewModelScope
    fun addToCart(product: Product) = viewModelScope.launch {
        repository.addToCart(product)
    }

    fun increaseQuantity(productId: Int) = viewModelScope.launch {
        repository.increaseQuantity(productId)
    }

    fun decreaseQuantity(productId: Int) = viewModelScope.launch {
        repository.decreaseQuantity(productId)
    }

    fun removeFromCart(productId: Int) = viewModelScope.launch {
        repository.removeFromCart(productId)
    }

    fun clearCart() = viewModelScope.launch {
        repository.clearCart()
    }
}