package com.example.minigrocery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.minigrocery.data.model.Category
import com.example.minigrocery.data.model.Product
import com.example.minigrocery.data.repository.ProductRepository

class HomeViewModel : ViewModel() {

    private val productRepository = ProductRepository()

    // Full product list (never modified — source of truth)
    private val allProducts = productRepository.getAllProducts()

    // What the RecyclerView actually displays
    private val _products = MutableLiveData<List<Product>>(allProducts)
    val products: LiveData<List<Product>> = _products

    // Category list
    private val _categories = MutableLiveData<List<Category>>(productRepository.getCategories())
    val categories: LiveData<List<Category>> = _categories

    // Tracks which category is selected (default = 1 = "All")
    private var selectedCategoryId: Int = 1
    private var currentSearchQuery: String = ""

    // Called when a category chip is tapped
    fun onCategorySelected(categoryId: Int) {
        selectedCategoryId = categoryId

        // Update the selected state on category list
        _categories.value = _categories.value?.map {
            it.copy(isSelected = it.id == categoryId)
        }

        applyFilters()
    }

    // Called on every keystroke in the search bar
    fun onSearchQueryChanged(query: String) {
        currentSearchQuery = query
        applyFilters()
    }

    // Combines both search + category filter
    private fun applyFilters() {
        var filtered = if (selectedCategoryId == 1) {
            allProducts
        } else {
            allProducts.filter { it.categoryId == selectedCategoryId }
        }

        if (currentSearchQuery.isNotBlank()) {
            val q = currentSearchQuery.lowercase()
            filtered = filtered.filter {
                it.name.lowercase().contains(q) ||
                        it.description.lowercase().contains(q)
            }
        }

        _products.value = filtered
    }
}