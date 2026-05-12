package com.example.minigrocery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CheckoutViewModel : ViewModel() {

    // Which payment option is selected
    enum class PaymentMethod { COD, ONLINE }

    private val _selectedPayment = MutableLiveData(PaymentMethod.COD)
    val selectedPayment: LiveData<PaymentMethod> = _selectedPayment

    fun selectPaymentMethod(method: PaymentMethod) {
        _selectedPayment.value = method
    }

    // Validates all checkout fields
    fun validateOrder(name: String, address: String, city: String): String? {
        return when {
            name.isBlank() -> "Please enter your full name"
            name.length < 3 -> "Name must be at least 3 characters"
            address.isBlank() -> "Please enter your delivery address"
            address.length < 10 -> "Please enter a complete address"
            city.isBlank() -> "Please enter your city or area"
            else -> null // null means valid
        }
    }

    // Generates a random order ID like "QB-48291"
    fun generateOrderId(): String {
        val number = (10000..99999).random()
        return "QB-$number"
    }
}