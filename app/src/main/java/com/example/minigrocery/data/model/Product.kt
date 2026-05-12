package com.example.minigrocery.data.model

/**
 * Represents a single grocery product shown in the product list.
 * imageRes is a drawable resource ID (R.drawable.xxx).
 */
data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val originalPrice: Double,
    val imageRes: Int,
    val categoryId: Int,
    val unit: String,
    val isAvailable: Boolean = true,
    val discountPercent: Int = 0
)