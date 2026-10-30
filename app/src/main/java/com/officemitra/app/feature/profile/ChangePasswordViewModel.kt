package com.officemitra.app.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.officemitra.app.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChangePasswordUiState(
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    fun onNewPasswordChanged(value: String) { _uiState.value = _uiState.value.copy(newPassword = value, errorMessage = null) }
    fun onConfirmPasswordChanged(value: String) { _uiState.value = _uiState.value.copy(confirmPassword = value, errorMessage = null) }

    fun updatePassword() {
        val state = _uiState.value
        if (state.newPassword.length < 6) {
            _uiState.value = _uiState.value.copy(errorMessage = "New password must be at least 6 characters")
            return
        }
        if (state.newPassword != state.confirmPassword) {
            _uiState.value = _uiState.value.copy(errorMessage = "Passwords do not match")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = authRepository.updatePassword(state.newPassword)
            _uiState.value = _uiState.value.copy(isLoading = false)
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(isSuccess = true)
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        errorMessage = error.localizedMessage ?: "Failed to update password."
                    )
                }
            )
        }
    }
}
