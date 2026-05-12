package com.example.minigrocery.data.model

/**
 * Represents a product category shown in the horizontal
 * category list on the Home screen.
 */
data class Category(
    val id: Int,
    val name: String,
    val emoji: String,
    val isSelected: Boolean = false
)