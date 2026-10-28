package com.officemitra.app.feature.company

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.officemitra.app.data.model.CompanyInfo
import com.officemitra.app.data.repository.OfficeMitraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class JoinCompanyUiState(
    val companyCode: String = "",
    val employeeId: String = "",
    val fullName: String = "",
    val mobileNumber: String = "",
    val matchedCompany: CompanyInfo? = null,
    val codeError: String? = null,
    val isLoading: Boolean = false,
    val isSubmitted: Boolean = false
)

@HiltViewModel
class JoinCompanyViewModel @Inject constructor(
    private val repository: OfficeMitraRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(JoinCompanyUiState())
    val uiState: StateFlow<JoinCompanyUiState> = _uiState.asStateFlow()

    fun onCompanyCodeChanged(code: String) {
        _uiState.value = _uiState.value.copy(
            companyCode = code,
            codeError = null
        )
        if (code.trim().length >= 4) {
            viewModelScope.launch {
                val result = repository.lookupCompanyByCode(code.trim())
                result.fold(
                    onSuccess = { companyInfo ->
                        _uiState.value = _uiState.value.copy(matchedCompany = companyInfo)
                    },
                    onFailure = {
                        _uiState.value = _uiState.value.copy(matchedCompany = null)
                    }
                )
            }
        } else {
            _uiState.value = _uiState.value.copy(matchedCompany = null)
        }
    }

    fun onEmployeeIdChanged(id: String) { _uiState.value = _uiState.value.copy(employeeId = id) }
    fun onFullNameChanged(name: String) { _uiState.value = _uiState.value.copy(fullName = name) }
    fun onMobileChanged(mobile: String) { _uiState.value = _uiState.value.copy(mobileNumber = mobile) }

    fun submitJoinRequest(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.companyCode.trim().isBlank()) {
            _uiState.value = _uiState.value.copy(codeError = "Company Code is required")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, codeError = null)
            val result = repository.joinCompanyByCode(
                companyCode = state.companyCode.trim(),
                department = "General",
                designation = "Employee",
                employeeId = state.employeeId.trim()
            )
            _uiState.value = _uiState.value.copy(isLoading = false)
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(isSubmitted = true)
                    onSuccess()
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        codeError = error.localizedMessage ?: "Failed to join company. Invalid code."
                    )
                }
            )
        }
    }
}
