package com.example.minigrocery.ui.success

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.minigrocery.R
import com.example.minigrocery.databinding.FragmentSuccessBinding
import com.example.minigrocery.utils.showToast

class SuccessFragment : Fragment() {

    private var _binding: FragmentSuccessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Read arguments passed from CheckoutFragment
        arguments?.let { args ->
            binding.tvOrderId.text = args.getString("orderId", "QB-00000")
            binding.tvOrderTotal.text = args.getString("orderTotal", "₹0")
            binding.tvPaymentMethod.text = args.getString("paymentMethod", "Cash on Delivery")
            binding.tvDeliveryAddress.text = args.getString("address", "—")
        }

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnContinueShopping.setOnClickListener {
            // Pop back to Home, clear success from back stack
            findNavController().navigate(R.id.action_success_to_home)
        }

        binding.btnTrackOrder.setOnClickListener {
            // Mock — just show a toast
            requireContext().showToast("Tracking coming soon! Your order is on the way 🛵")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}