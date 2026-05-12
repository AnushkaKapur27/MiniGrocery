package com.example.minigrocery.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.example.minigrocery.utils.Constants.CURRENCY_SYMBOL

// Show a short toast from any Context
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

// Toggle visibility cleanly
fun View.show() { visibility = View.VISIBLE }
fun View.hide() { visibility = View.GONE }

// Format a Double as a currency string — e.g. 149.0 → "₹149"
fun Double.toCurrencyString(): String {
    return if (this % 1.0 == 0.0) {
        "$CURRENCY_SYMBOL${this.toInt()}"
    } else {
        "$CURRENCY_SYMBOL${"%.2f".format(this)}"
    }
}