package com.example.minigrocery.utils

import com.example.minigrocery.R
import com.example.minigrocery.data.model.Category
import com.example.minigrocery.data.model.Product

/**
 * All mock data lives here.
 * In a real app this would come from a REST API.
 * For this project, this is our "backend".
 */
object SampleData {

    // ── CATEGORIES ──────────────────────────────────────────

    fun getCategories(): List<Category> = listOf(
        Category(id = 1, name = "All",        emoji = "🛒"),
        Category(id = 2, name = "Fruits",     emoji = "🍎"),
        Category(id = 3, name = "Vegetables", emoji = "🥦"),
        Category(id = 4, name = "Dairy",      emoji = "🥛"),
        Category(id = 5, name = "Snacks",     emoji = "🍿"),
        Category(id = 6, name = "Beverages",  emoji = "🧃")
    )

    // ── PRODUCTS ─────────────────────────────────────────────
    // imageRes uses Android's built-in placeholder for now.
    // In Phase 3 we will replace with real drawable assets.

    fun getProducts(): List<Product> = listOf(

        // FRUITS (categoryId = 2)
        Product(
            id = 1,
            name = "Fresh Apples",
            description = "Crisp and sweet red apples, farm fresh",
            price = 149.0,
            originalPrice = 180.0,
            imageRes = R.drawable.ic_placeholder,
            categoryId = 2,
            unit = "1 kg",
            discountPercent = 17
        ),
        Product(
            id = 2,
            name = "Bananas",
            description = "Ripe yellow bananas, rich in potassium",
            price = 49.0,
            originalPrice = 60.0,
            imageRes = R.drawable.ic_placeholder,
            categoryId = 2,
            unit = "Dozen",
            discountPercent = 18
        ),
        Product(
            id = 3,
            name = "Watermelon",
            description = "Sweet and juicy, perfect for summer",
            price = 89.0,
            originalPrice = 89.0,
            imageRes = R.drawable.ic_placeholder,
            categoryId = 2,
            unit = "Per piece",
            discountPercent = 0
        ),

        // VEGETABLES (categoryId = 3)
        Product(
            id = 4,
            name = "Tomatoes",
            description = "Farm fresh red tomatoes",
            price = 39.0,
            originalPrice = 50.0,
            imageRes = R.drawable.ic_placeholder,
            categoryId = 3,
            unit = "500 g",
            discountPercent = 22
        ),
        Product(
            id = 5,
            name = "Spinach",
            description = "Tender baby spinach leaves, washed & ready",
            price = 29.0,
            originalPrice = 35.0,
            imageRes = R.drawable.ic_placeholder,
            categoryId = 3,
            unit = "200 g",
            discountPercent = 17
        ),
        Product(
            id = 6,
            name = "Onions",
            description = "Fresh red onions, essential for every kitchen",
            price = 35.0,
            originalPrice = 40.0,
            imageRes = R.drawable.ic_placeholder,
            categoryId = 3,
            unit = "1 kg",
            discountPercent = 12
        ),

        // DAIRY (categoryId = 4)
        Product(
            id = 7,
            name = "Amul Full Cream Milk",
            description = "Pure fresh full cream milk, 6% fat",
            price = 31.0,
            originalPrice = 31.0,
            imageRes = R.drawable.ic_placeholder,
            categoryId = 4,
            unit = "500 ml",
            discountPercent = 0
        ),
        Product(
            id = 8,
            name = "Amul Butter",
            description = "Pasteurised butter, rich and creamy",
            price = 56.0,
            originalPrice = 60.0,
            imageRes = R.drawable.ic_placeholder,
            categoryId = 4,
            unit = "100 g",
            discountPercent = 7
        ),
        Product(
            id = 9,
            name = "Paneer",
            description = "Soft and fresh cottage cheese",
            price = 89.0,
            originalPrice = 100.0,
            imageRes = R.drawable.ic_placeholder,
            categoryId = 4,
            unit = "200 g",
            discountPercent = 11
        ),

        // SNACKS (categoryId = 5)
        Product(
            id = 10,
            name = "Lay's Classic Salted",
            description = "Classic crispy potato chips",
            price = 20.0,
            originalPrice = 20.0,
            imageRes = R.drawable.ic_placeholder,
            categoryId = 5,
            unit = "26 g",
            discountPercent = 0
        ),
        Product(
            id = 11,
            name = "Bingo Mad Angles",
            description = "Achaari masti triangular chips",
            price = 20.0,
            originalPrice = 20.0,
            imageRes = R.drawable.ic_placeholder,
            categoryId = 5,
            unit = "30 g",
            discountPercent = 0
        ),
        Product(
            id = 12,
            name = "Haldiram's Aloo Bhujia",
            description = "Crispy namkeen, a family favourite",
            price = 55.0,
            originalPrice = 60.0,
            imageRes = R.drawable.ic_placeholder,
            categoryId = 5,
            unit = "150 g",
            discountPercent = 8
        ),

        // BEVERAGES (categoryId = 6)
        Product(
            id = 13,
            name = "Tropicana Orange Juice",
            description = "100% pure squeezed orange juice",
            price = 99.0,
            originalPrice = 120.0,
            imageRes = R.drawable.ic_placeholder,
            categoryId = 6,
            unit = "1 litre",
            discountPercent = 17
        ),
        Product(
            id = 14,
            name = "Coca-Cola",
            description = "The classic refreshing cola drink",
            price = 40.0,
            originalPrice = 40.0,
            imageRes = R.drawable.ic_placeholder,
            categoryId = 6,
            unit = "750 ml",
            discountPercent = 0
        ),
        Product(
            id = 15,
            name = "Red Bull Energy Drink",
            description = "Gives you wings — original energy drink",
            price = 125.0,
            originalPrice = 135.0,
            imageRes = R.drawable.ic_placeholder,
            categoryId = 6,
            unit = "250 ml",
            discountPercent = 7
        )
    )

    // Filter products by category
    fun getProductsByCategory(categoryId: Int): List<Product> {
        if (categoryId == 1) return getProducts() // "All" category
        return getProducts().filter { it.categoryId == categoryId }
    }

    // Search products by name or description
    fun searchProducts(query: String): List<Product> {
        val q = query.lowercase().trim()
        if (q.isEmpty()) return getProducts()
        return getProducts().filter {
            it.name.lowercase().contains(q) ||
                    it.description.lowercase().contains(q)
        }
    }
}