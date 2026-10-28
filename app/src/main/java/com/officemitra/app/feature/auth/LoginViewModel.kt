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

data class LoginUiState(
    val emailOrPhone: String = "",
    val password: String = "",
    val rememberMe: Boolean = true,
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailOrPhoneChanged(value: String) {
        _uiState.value = _uiState.value.copy(emailOrPhone = value, emailError = null, errorMessage = null)
    }

    fun onPasswordChanged(value: String) {
        _uiState.value = _uiState.value.copy(password = value, passwordError = null, errorMessage = null)
    }

    fun onRememberMeChanged(value: Boolean) {
        _uiState.value = _uiState.value.copy(rememberMe = value)
    }

    fun login(onSuccess: () -> Unit) {
        val state = _uiState.value
        var hasError = false

        if (state.emailOrPhone.isBlank()) {
            _uiState.value = _uiState.value.copy(emailError = "Email or mobile number is required")
            hasError = true
        }

        if (state.password.isBlank()) {
            _uiState.value = _uiState.value.copy(passwordError = "Password is required")
            hasError = true
        } else if (state.password.length < 4) {
            _uiState.value = _uiState.value.copy(passwordError = "Password must be at least 4 characters")
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = authRepository.signIn(
                email = state.emailOrPhone.trim(),
                pass = state.password
            )
            _uiState.value = _uiState.value.copy(isLoading = false)
            result.fold(
                onSuccess = { onSuccess() },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        errorMessage = error.localizedMessage ?: "Login failed. Please check your credentials."
                    )
                }
            )
        }
    }
}
