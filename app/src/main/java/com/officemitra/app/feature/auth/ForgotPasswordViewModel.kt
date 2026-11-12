package com.officemitra.app.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.officemitra.app.data.auth.AuthAction
import com.officemitra.app.data.auth.AuthErrorMapper
import com.officemitra.app.data.auth.AuthValidator
import com.officemitra.app.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ForgotPasswordUiState(
    val email: String = "",
    val emailError: String? = null,
    val isLoading: Boolean = false,
    val isSubmitted: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    fun onEmailChanged(value: String) {
        _uiState.update { it.copy(email = value, emailError = null, errorMessage = null) }
    }

    fun sendPasswordReset() {
        val state = _uiState.value
        if (state.isLoading) return

        val emailError = AuthValidator.emailError(state.email)
        if (emailError != null) {
            _uiState.update { it.copy(emailError = emailError) }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            // Supabase answers the same way whether or not an account exists for the address, and
            // the confirmation shown to the user is worded accordingly: this screen must not
            // reveal which email addresses are registered.
            authRepository.sendPasswordReset(state.email).fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isSubmitted = true) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = AuthErrorMapper.userMessage(error, AuthAction.PASSWORD_RESET_REQUEST)
                        )
                    }
                }
            )
        }
    }

    /** Lets the user correct the address or request another link. */
    fun editEmail() {
        _uiState.update { it.copy(isSubmitted = false, errorMessage = null) }
    }
}
