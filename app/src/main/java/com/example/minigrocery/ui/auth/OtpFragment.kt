package com.example.minigrocery.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.minigrocery.R
import com.example.minigrocery.databinding.FragmentOtpBinding
import com.example.minigrocery.utils.hide
import com.example.minigrocery.utils.show
import com.example.minigrocery.utils.showToast
import com.example.minigrocery.viewmodel.AuthViewModel

class OtpFragment : Fragment() {

    private var _binding: FragmentOtpBinding? = null
    private val binding get() = _binding!!

    // IMPORTANT: same viewModels() call shares the SAME instance
    // as LoginFragment because they're in the same back stack scope
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPhoneHint()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupPhoneHint() {
        // Show the phone number the OTP was "sent to"
        authViewModel.phoneNumber.observe(viewLifecycleOwner) { phone ->
            if (!phone.isNullOrEmpty()) {
                binding.tvPhoneHint.text = "OTP sent to +91 $phone"
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnVerify.setOnClickListener {
            binding.tvError.hide()
            val otp = binding.etOtp.text.toString().trim()
            authViewModel.verifyOtp(otp)
        }

        binding.tvResend.setOnClickListener {
            requireContext().showToast("OTP resent! Use 1234")
        }
    }

    private fun observeViewModel() {
        authViewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthViewModel.AuthState.Loading -> {
                    binding.progressBar.show()
                    binding.btnVerify.isEnabled = false
                    binding.tvError.hide()
                }
                is AuthViewModel.AuthState.OtpVerified -> {
                    binding.progressBar.hide()
                    // Navigate to Home, clear back stack
                    findNavController().navigate(R.id.action_otp_to_home)
                    authViewModel.resetState()
                }
                is AuthViewModel.AuthState.Error -> {
                    binding.progressBar.hide()
                    binding.btnVerify.isEnabled = true
                    binding.tvError.text = state.message
                    binding.tvError.show()
                }
                else -> {
                    binding.progressBar.hide()
                    binding.btnVerify.isEnabled = true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}