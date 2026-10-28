package com.officemitra.app.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.officemitra.app.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val fullName: String = "",
    val email: String = "",
    val mobileNumber: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val acceptTerms: Boolean = false,
    val nameError: String? = null,
    val emailError: String? = null,
    val mobileError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val termsError: String? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onFullNameChanged(value: String) { _uiState.value = _uiState.value.copy(fullName = value, nameError = null) }
    fun onEmailChanged(value: String) { _uiState.value = _uiState.value.copy(email = value, emailError = null) }
    fun onMobileNumberChanged(value: String) { _uiState.value = _uiState.value.copy(mobileNumber = value, mobileError = null) }
    fun onPasswordChanged(value: String) { _uiState.value = _uiState.value.copy(password = value, passwordError = null) }
    fun onConfirmPasswordChanged(value: String) { _uiState.value = _uiState.value.copy(confirmPassword = value, confirmPasswordError = null) }
    fun onAcceptTermsChanged(value: Boolean) { _uiState.value = _uiState.value.copy(acceptTerms = value, termsError = null) }

    fun register(onSuccess: () -> Unit) {
        val state = _uiState.value
        var hasError = false

        if (state.fullName.isBlank()) {
            _uiState.value = _uiState.value.copy(nameError = "Full Name is required")
            hasError = true
        }
        if (state.email.isBlank() || !state.email.contains("@")) {
            _uiState.value = _uiState.value.copy(emailError = "Valid email address is required")
            hasError = true
        }
        if (state.mobileNumber.isBlank()) {
            _uiState.value = _uiState.value.copy(mobileError = "Mobile Number is required")
            hasError = true
        }
        if (state.password.length < 6) {
            _uiState.value = _uiState.value.copy(passwordError = "Password must be at least 6 characters")
            hasError = true
        }
        if (state.confirmPassword != state.password) {
            _uiState.value = _uiState.value.copy(confirmPasswordError = "Passwords do not match")
            hasError = true
        }
        if (!state.acceptTerms) {
            _uiState.value = _uiState.value.copy(termsError = "You must accept the terms and conditions")
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = authRepository.signUp(
                email = state.email.trim(),
                pass = state.password,
                fullName = state.fullName.trim(),
                mobile = state.mobileNumber.trim()
            )
            _uiState.value = _uiState.value.copy(isLoading = false)
            result.fold(
                onSuccess = { onSuccess() },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        errorMessage = error.localizedMessage ?: "Registration failed. Please try again."
                    )
                }
            )
        }
    }
}
