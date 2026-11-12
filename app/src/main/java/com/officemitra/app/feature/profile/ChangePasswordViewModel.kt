package com.officemitra.app.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.officemitra.app.data.auth.AuthAction
import com.officemitra.app.data.auth.AuthErrorMapper
import com.officemitra.app.data.auth.AuthErrorType
import com.officemitra.app.data.auth.AuthValidator
import com.officemitra.app.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChangePasswordUiState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val currentPasswordError: String? = null,
    val newPasswordError: String? = null,
    val confirmPasswordError: String? = null,
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

    fun onCurrentPasswordChanged(value: String) = _uiState.update {
        it.copy(currentPassword = value, currentPasswordError = null, errorMessage = null)
    }

    fun onNewPasswordChanged(value: String) = _uiState.update {
        it.copy(newPassword = value, newPasswordError = null, errorMessage = null)
    }

    fun onConfirmPasswordChanged(value: String) = _uiState.update {
        it.copy(confirmPassword = value, confirmPasswordError = null, errorMessage = null)
    }

    /**
     * @param isRecovery true when the screen was opened from a password-reset email. The reset link
     * already proved who the user is, so the current password is not asked for. In every other
     * case the current password is verified before anything is changed.
     */
    fun updatePassword(isRecovery: Boolean) {
        val state = _uiState.value
        if (state.isLoading || state.isSuccess) return

        val currentError = when {
            isRecovery -> null
            state.currentPassword.isEmpty() -> "Current password is required"
            else -> null
        }
        val newError = AuthValidator.newPasswordError(state.newPassword)
            ?: if (!isRecovery && state.newPassword == state.currentPassword) {
                "New password must be different from the current password"
            } else null
        val confirmError = AuthValidator.confirmPasswordError(state.newPassword, state.confirmPassword)

        if (currentError != null || newError != null || confirmError != null) {
            _uiState.update {
                it.copy(
                    currentPasswordError = currentError,
                    newPasswordError = newError,
                    confirmPasswordError = confirmError
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            val result = if (isRecovery) {
                authRepository.completePasswordRecovery(state.newPassword)
            } else {
                authRepository.changePassword(state.currentPassword, state.newPassword)
            }
            result.fold(
                onSuccess = {
                    // Success is reported only after Supabase accepted the new password.
                    _uiState.value = ChangePasswordUiState(isSuccess = true)
                },
                onFailure = { error ->
                    val mapped = AuthErrorMapper.map(error, AuthAction.UPDATE_PASSWORD)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = mapped.message,
                            currentPasswordError = if (mapped.type == AuthErrorType.INVALID_CREDENTIALS) mapped.message else null,
                            newPasswordError = if (mapped.type == AuthErrorType.WEAK_PASSWORD || mapped.type == AuthErrorType.SAME_PASSWORD) mapped.message else null
                        )
                    }
                }
            )
        }
    }
}
