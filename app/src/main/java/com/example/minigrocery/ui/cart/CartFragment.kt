package com.example.minigrocery.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minigrocery.R
import com.example.minigrocery.adapter.CartAdapter
import com.example.minigrocery.databinding.FragmentCartBinding
import com.example.minigrocery.utils.hide
import com.example.minigrocery.utils.show
import com.example.minigrocery.utils.toCurrencyString
import com.example.minigrocery.viewmodel.CartViewModel

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    // Shared with HomeFragment — same instance
    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            onIncrease = { productId -> cartViewModel.increaseQuantity(productId) },
            onDecrease = { productId -> cartViewModel.decreaseQuantity(productId) }
        )
        binding.rvCartItems.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupClickListeners() {
        binding.btnCheckout.setOnClickListener {
            findNavController().navigate(R.id.action_cart_to_checkout)
        }

        binding.btnShopNow.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeViewModel() {
        cartViewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            cartAdapter.submitList(cartItems)

            if (cartItems.isEmpty()) {
                // Show empty state
                binding.layoutEmptyCart.show()
                binding.layoutCartContent.hide()
                binding.cardCheckoutBtn.hide()
            } else {
                // Show cart content
                binding.layoutEmptyCart.hide()
                binding.layoutCartContent.show()
                binding.cardCheckoutBtn.show()

                // Update bill summary
                val subtotal = cartViewModel.getSubtotal(cartItems)
                val deliveryFee = cartViewModel.getDeliveryFee(subtotal)
                val total = cartViewModel.getTotal(cartItems)

                binding.tvSubtotal.text = subtotal.toCurrencyString()
                binding.tvDeliveryFee.text = if (deliveryFee == 0.0) "FREE" else deliveryFee.toCurrencyString()
                binding.tvTotal.text = total.toCurrencyString()

                // Update items label
                binding.tvItemsLabel.text = "${cartItems.size} item(s) in your cart"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}