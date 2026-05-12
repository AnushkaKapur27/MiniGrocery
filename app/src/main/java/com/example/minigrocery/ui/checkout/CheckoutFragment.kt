package com.example.minigrocery.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.minigrocery.R
import com.example.minigrocery.databinding.FragmentCheckoutBinding
import com.example.minigrocery.utils.showToast
import com.example.minigrocery.utils.toCurrencyString
import com.example.minigrocery.viewmodel.CartViewModel
import com.example.minigrocery.viewmodel.CheckoutViewModel

class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private val cartViewModel: CartViewModel by activityViewModels()
    private val checkoutViewModel: CheckoutViewModel by viewModels()

    // Store the generated order ID so Success screen can show it
    private var generatedOrderId = ""
    private var orderTotal = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupPaymentOptions()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupPaymentOptions() {
        binding.layoutCod.setOnClickListener {
            binding.rbCod.isChecked = true
            binding.rbOnline.isChecked = false
            checkoutViewModel.selectPaymentMethod(CheckoutViewModel.PaymentMethod.COD)
            updatePaymentUI(CheckoutViewModel.PaymentMethod.COD)
        }

        binding.layoutOnline.setOnClickListener {
            binding.rbOnline.isChecked = true
            binding.rbCod.isChecked = false
            checkoutViewModel.selectPaymentMethod(CheckoutViewModel.PaymentMethod.ONLINE)
            updatePaymentUI(CheckoutViewModel.PaymentMethod.ONLINE)
        }
    }

    private fun updatePaymentUI(method: CheckoutViewModel.PaymentMethod) {
        // Swap background drawables to show which is selected
        if (method == CheckoutViewModel.PaymentMethod.COD) {
            binding.layoutCod.setBackgroundResource(R.drawable.bg_payment_option_selected)
            binding.layoutOnline.setBackgroundResource(R.drawable.bg_payment_option)
        } else {
            binding.layoutOnline.setBackgroundResource(R.drawable.bg_payment_option_selected)
            binding.layoutCod.setBackgroundResource(R.drawable.bg_payment_option)
        }
    }

    private fun setupClickListeners() {
        binding.btnPlaceOrder.setOnClickListener {
            val name    = binding.etName.text.toString().trim()
            val address = binding.etAddress.text.toString().trim()
            val city    = binding.etCity.text.toString().trim()

            // Validate
            val error = checkoutViewModel.validateOrder(name, address, city)
            if (error != null) {
                requireContext().showToast(error)
                return@setOnClickListener
            }

            // Generate order details
            generatedOrderId = checkoutViewModel.generateOrderId()

            // Get total from cart
            val cartItems = cartViewModel.cartItems.value ?: emptyList()
            orderTotal = cartViewModel.getTotal(cartItems).toCurrencyString()

            // Clear cart — order placed!
            cartViewModel.clearCart()

            // Navigate to success screen, pass order details via Bundle
            val bundle = Bundle().apply {
                putString("orderId", generatedOrderId)
                putString("orderTotal", orderTotal)
                putString("paymentMethod", if (binding.rbCod.isChecked) "Cash on Delivery" else "Online Payment")
                putString("address", "$address, $city")
            }
            findNavController().navigate(R.id.action_checkout_to_success, bundle)
        }
    }

    private fun observeViewModel() {
        cartViewModel.cartItems.observe(viewLifecycleOwner) { items ->
            val total = cartViewModel.getTotal(items)
            binding.tvCheckoutTotal.text = total.toCurrencyString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}