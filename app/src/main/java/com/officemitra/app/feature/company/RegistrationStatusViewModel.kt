package com.officemitra.app.feature.company

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.officemitra.app.data.model.AccountLoadError
import com.officemitra.app.data.model.MembershipState
import com.officemitra.app.data.model.User
import com.officemitra.app.data.repository.OfficeMitraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegistrationStatusUiState(
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class RegistrationStatusViewModel @Inject constructor(
    private val repository: OfficeMitraRepository
) : ViewModel() {
    val currentUser: StateFlow<User> = repository.currentUser

    private val _uiState = MutableStateFlow(RegistrationStatusUiState())
    val uiState: StateFlow<RegistrationStatusUiState> = _uiState.asStateFlow()

    /**
     * Reloads the membership from the server.
     *
     * @param onNoMembership called when the server no longer has a join request for this user.
     */
    fun refreshStatus(onNoMembership: () -> Unit = {}) {
        if (_uiState.value.isRefreshing) return
        _uiState.value = RegistrationStatusUiState(isRefreshing = true)
        viewModelScope.launch {
            repository.refreshAll()
            val account = repository.accountState.value
            _uiState.value = RegistrationStatusUiState(
                errorMessage = when (account.loadError) {
                    AccountLoadError.NONE -> null
                    AccountLoadError.NETWORK -> "Couldn't check your status. Please check your internet connection and try again."
                    AccountLoadError.OTHER -> "Couldn't check your status. Please try again in a moment."
                }
            )
            if (account.loadError == AccountLoadError.NONE && account.membership == MembershipState.NONE) {
                onNoMembership()
            }
        }
    }
}
