package com.officemitra.app.feature.company

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.officemitra.app.data.auth.AuthAction
import com.officemitra.app.data.auth.AuthErrorMapper
import com.officemitra.app.data.auth.AuthValidator
import com.officemitra.app.data.model.CompanyInfo
import com.officemitra.app.data.repository.OfficeMitraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

data class JoinCompanyUiState(
    val companyCode: String = "",
    val employeeId: String = "",
    val fullName: String = "",
    val mobileNumber: String = "",
    val matchedCompany: CompanyInfo? = null,
    val codeError: String? = null,
    val nameError: String? = null,
    val mobileError: String? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val isSubmitted: Boolean = false
)

@HiltViewModel
class JoinCompanyViewModel @Inject constructor(
    private val repository: OfficeMitraRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(JoinCompanyUiState())
    val uiState: StateFlow<JoinCompanyUiState> = _uiState.asStateFlow()

    private var lookupJob: Job? = null
    private var nameEdited = false
    private var mobileEdited = false

    init {
        // Show the name and number the account was registered with; the user can correct them here.
        viewModelScope.launch {
            val user = withTimeoutOrNull(PROFILE_WAIT_MS) { repository.currentUser.first { it.id.isNotBlank() } }
                ?: return@launch
            _uiState.update {
                it.copy(
                    fullName = if (nameEdited) it.fullName else user.name,
                    mobileNumber = if (mobileEdited) it.mobileNumber else user.phone
                )
            }
        }
    }

    fun onCompanyCodeChanged(code: String) {
        _uiState.update { it.copy(companyCode = code, codeError = null, errorMessage = null, matchedCompany = null) }

        // Only the latest code is looked up: an older, slower request can never overwrite the result.
        lookupJob?.cancel()
        val cleanCode = code.trim()
        if (cleanCode.length < MIN_CODE_LENGTH) return
        lookupJob = viewModelScope.launch {
            delay(LOOKUP_DEBOUNCE_MS)
            val company = repository.lookupCompanyByCode(cleanCode).getOrNull()
            _uiState.update {
                if (it.companyCode.trim() == cleanCode) it.copy(matchedCompany = company) else it
            }
        }
    }

    fun onEmployeeIdChanged(id: String) = _uiState.update { it.copy(employeeId = id, errorMessage = null) }

    fun onFullNameChanged(name: String) {
        nameEdited = true
        _uiState.update { it.copy(fullName = name, nameError = null, errorMessage = null) }
    }

    fun onMobileChanged(mobile: String) {
        mobileEdited = true
        _uiState.update { it.copy(mobileNumber = mobile, mobileError = null, errorMessage = null) }
    }

    fun submitJoinRequest(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.isLoading || state.isSubmitted) return

        val codeError = if (state.companyCode.isBlank()) "Company Code is required" else null
        val nameError = AuthValidator.fullNameError(state.fullName)
        val mobileError = AuthValidator.phoneError(state.mobileNumber)
        if (codeError != null || nameError != null || mobileError != null) {
            _uiState.update { it.copy(codeError = codeError, nameError = nameError, mobileError = mobileError) }
            return
        }

        _uiState.update { it.copy(isLoading = true, codeError = null, errorMessage = null) }
        viewModelScope.launch {
            // The name and number on this form are the applicant's own profile; store what was typed.
            val user = repository.currentUser.value
            val profileChanged = state.fullName.trim() != user.name ||
                AuthValidator.normalizePhone(state.mobileNumber) != AuthValidator.normalizePhone(user.phone)
            if (profileChanged) {
                val profileError = repository.updateProfile(state.fullName, user.email, state.mobileNumber).exceptionOrNull()
                if (profileError != null) {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = AuthErrorMapper.userMessage(profileError, AuthAction.PROFILE))
                    }
                    return@launch
                }
            }

            val result = repository.joinCompanyByCode(
                companyCode = state.companyCode.trim(),
                department = "General",
                designation = "Employee",
                employeeId = state.employeeId.trim()
            )
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isSubmitted = true) }
                    onSuccess()
                },
                onFailure = { error ->
                    // Only messages written for the user are shown; never raw server or network text.
                    _uiState.update {
                        it.copy(isLoading = false, codeError = AuthErrorMapper.userMessage(error, AuthAction.DATA))
                    }
                }
            )
        }
    }

    private companion object {
        const val MIN_CODE_LENGTH = 4
        const val LOOKUP_DEBOUNCE_MS = 400L
        const val PROFILE_WAIT_MS = 10_000L
    }
}
