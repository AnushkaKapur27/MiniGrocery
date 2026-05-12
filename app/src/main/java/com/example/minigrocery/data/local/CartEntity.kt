package com.example.minigrocery.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This IS the Room database table — one row per product in cart.
 * We only store productId + quantity to keep the DB simple.
 * The full Product data lives in SampleData.kt (mock source).
 */
@Entity(tableName = "cart_table")
data class CartEntity(
    @PrimaryKey
    val productId: Int,         // matches Product.id
    val quantity: Int
)