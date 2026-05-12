package com.example.minigrocery.data.repository

import com.example.minigrocery.data.model.Category
import com.example.minigrocery.data.model.Product
import com.example.minigrocery.utils.SampleData

/**
 * ProductRepository wraps SampleData.
 * In a real app, this would make Retrofit API calls.
 * The ViewModel doesn't know or care — it just calls these functions.
 */
class ProductRepository {

    fun getCategories(): List<Category> = SampleData.getCategories()

    fun getAllProducts(): List<Product> = SampleData.getProducts()

    fun getProductsByCategory(categoryId: Int): List<Product> =
        SampleData.getProductsByCategory(categoryId)

    fun searchProducts(query: String): List<Product> =
        SampleData.searchProducts(query)
}