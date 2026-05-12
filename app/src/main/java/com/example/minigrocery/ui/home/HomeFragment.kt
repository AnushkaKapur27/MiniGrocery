package com.example.minigrocery.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minigrocery.R
import com.example.minigrocery.adapter.CategoryAdapter
import com.example.minigrocery.adapter.ProductAdapter
import com.example.minigrocery.databinding.FragmentHomeBinding
import com.example.minigrocery.utils.hide
import com.example.minigrocery.utils.show
import com.example.minigrocery.viewmodel.CartViewModel
import com.example.minigrocery.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()

    // activityViewModels() shares CartViewModel across ALL fragments
    // so cart state is consistent everywhere in the app
    private val cartViewModel: CartViewModel by activityViewModels()

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        setupSearchBar()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerViews() {
        // Category RecyclerView — horizontal
        categoryAdapter = CategoryAdapter { category ->
            homeViewModel.onCategorySelected(category.id)
        }
        binding.rvCategories.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }

        // Product RecyclerView — vertical list
        productAdapter = ProductAdapter { product ->
            cartViewModel.addToCart(product)
        }
        binding.rvProducts.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupSearchBar() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                homeViewModel.onSearchQueryChanged(query)

                // Show/hide clear button
                if (query.isNotEmpty()) {
                    binding.ivClearSearch.show()
                } else {
                    binding.ivClearSearch.hide()
                }
            }
        })

        binding.ivClearSearch.setOnClickListener {
            binding.etSearch.setText("")
        }
    }

    private fun setupClickListeners() {
        binding.btnCart.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_cart)
        }
    }

    private fun observeViewModel() {
        // Observe category list
        homeViewModel.categories.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.submitList(categories)
        }

        // Observe product list
        homeViewModel.products.observe(viewLifecycleOwner) { products ->
            productAdapter.submitList(products)

            // Update count label
            binding.tvProductCount.text = "${products.size} items"

            // Show empty state if no results
            if (products.isEmpty()) {
                binding.layoutEmpty.show()
                binding.rvProducts.hide()
            } else {
                binding.layoutEmpty.hide()
                binding.rvProducts.show()
            }
        }

        // Observe cart item count for badge
        cartViewModel.totalItemCount.observe(viewLifecycleOwner) { count ->
            if (count > 0) {
                binding.tvCartBadge.show()
                binding.tvCartBadge.text = if (count > 99) "99+" else count.toString()
            } else {
                binding.tvCartBadge.hide()
            }
        }

        // Observe cart items to update ADD/ADDED button state
        cartViewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            val cartProductIds = cartItems.map { it.product.id }.toSet()
            productAdapter.updateCartItems(cartProductIds)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}