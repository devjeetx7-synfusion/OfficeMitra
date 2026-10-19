package com.officemitra.app.feature.company

import androidx.lifecycle.ViewModel
import com.officemitra.app.data.model.CompanyInfo
import com.officemitra.app.data.repository.DemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class JoinCompanyUiState(
    val companyCode: String = "",
    val employeeId: String = "",
    val fullName: String = "Alex Morgan",
    val mobileNumber: String = "+1 555-019-2834",
    val matchedCompany: CompanyInfo? = null,
    val codeError: String? = null,
    val isSubmitted: Boolean = false
)

@HiltViewModel
class JoinCompanyViewModel @Inject constructor(
    private val repository: DemoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(JoinCompanyUiState())
    val uiState: StateFlow<JoinCompanyUiState> = _uiState.asStateFlow()

    fun onCompanyCodeChanged(code: String) {
        _uiState.value = _uiState.value.copy(
            companyCode = code,
            codeError = null,
            matchedCompany = if (code.trim().equals(repository.validCompanyCode, ignoreCase = true)) {
                repository.demoCompanyInfo
            } else null
        )
    }

    fun onEmployeeIdChanged(id: String) { _uiState.value = _uiState.value.copy(employeeId = id) }
    fun onFullNameChanged(name: String) { _uiState.value = _uiState.value.copy(fullName = name) }
    fun onMobileChanged(mobile: String) { _uiState.value = _uiState.value.copy(mobileNumber = mobile) }

    fun submitJoinRequest(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.companyCode.trim().equals(repository.validCompanyCode, ignoreCase = true)) {
            _uiState.value = _uiState.value.copy(isSubmitted = true)
            onSuccess()
        } else {
            _uiState.value = _uiState.value.copy(codeError = "Invalid Demo Company Code. Try 'OM-10245'")
        }
    }
}
