package com.example.minigrocery.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.minigrocery.R
import com.example.minigrocery.databinding.FragmentLoginBinding
import com.example.minigrocery.utils.hide
import com.example.minigrocery.utils.show
import com.example.minigrocery.utils.showToast
import com.example.minigrocery.viewmodel.AuthViewModel

class LoginFragment : Fragment() {

    // ViewBinding — no more findViewById
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // viewModels() scopes the ViewModel to this Fragment
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnSendOtp.setOnClickListener {
            // Clear previous errors
            binding.tilPhone.error = null
            binding.tvError.hide()

            val phone = binding.etPhone.text.toString().trim()
            authViewModel.sendOtp(phone)
        }
    }

    private fun observeViewModel() {
        authViewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthViewModel.AuthState.Idle -> {
                    binding.progressBar.hide()
                    binding.btnSendOtp.isEnabled = true
                }
                is AuthViewModel.AuthState.Loading -> {
                    binding.progressBar.show()
                    binding.btnSendOtp.isEnabled = false
                    binding.tvError.hide()
                }
                is AuthViewModel.AuthState.OtpSent -> {
                    binding.progressBar.hide()
                    binding.btnSendOtp.isEnabled = true
                    // Navigate to OTP screen
                    findNavController().navigate(R.id.action_login_to_otp)
                    authViewModel.resetState()
                }
                is AuthViewModel.AuthState.Error -> {
                    binding.progressBar.hide()
                    binding.btnSendOtp.isEnabled = true
                    binding.tvError.text = state.message
                    binding.tvError.show()
                }
                else -> Unit
            }
        }
    }

    // Always clean up binding in onDestroyView to avoid memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}