package com.example.minigrocery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Handles all logic for Login + OTP screens.
 * The Fragment only observes — it never makes decisions itself.
 */
class AuthViewModel : ViewModel() {

    // Stores the phone number entered on LoginFragment
    // OtpFragment reads this to know which number to "verify"
    private val _phoneNumber = MutableLiveData<String>()
    val phoneNumber: LiveData<String> = _phoneNumber

    // Sealed class — every possible UI state is explicitly modelled
    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        object OtpSent : AuthState()        // phone valid → navigate to OTP screen
        object OtpVerified : AuthState()    // OTP correct → navigate to Home
        data class Error(val message: String) : AuthState()
    }

    private val _authState = MutableLiveData<AuthState>(AuthState.Idle)
    val authState: LiveData<AuthState> = _authState

    // Called when user taps "Send OTP"
    fun sendOtp(phone: String) {
        when {
            phone.isBlank() -> {
                _authState.value = AuthState.Error("Please enter your mobile number")
            }
            phone.length != 10 -> {
                _authState.value = AuthState.Error("Enter a valid 10-digit mobile number")
            }
            !phone.all { it.isDigit() } -> {
                _authState.value = AuthState.Error("Mobile number must contain only digits")
            }
            else -> {
                _phoneNumber.value = phone
                _authState.value = AuthState.Loading
                // Simulate network delay for sending OTP
                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                    _authState.value = AuthState.OtpSent
                }, 1500)
            }
        }
    }

    // Called when user taps "Verify OTP"
    fun verifyOtp(enteredOtp: String) {
        when {
            enteredOtp.isBlank() -> {
                _authState.value = AuthState.Error("Please enter the OTP")
            }
            enteredOtp.length != 4 -> {
                _authState.value = AuthState.Error("OTP must be 4 digits")
            }
            enteredOtp != com.example.minigrocery.utils.Constants.FAKE_OTP -> {
                _authState.value = AuthState.Error("Invalid OTP. Use 1234")
            }
            else -> {
                _authState.value = AuthState.Loading
                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                    _authState.value = AuthState.OtpVerified
                }, 1000)
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}