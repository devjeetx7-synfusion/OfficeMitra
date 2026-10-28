package com.officemitra.app.feature.approvals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.officemitra.app.data.model.ApprovalRequest
import com.officemitra.app.data.model.User
import com.officemitra.app.data.repository.OfficeMitraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ApprovalsUiState(
    val currentUser: User,
    val approvalRequests: List<ApprovalRequest> = emptyList()
)

@HiltViewModel
class ApprovalsViewModel @Inject constructor(
    private val repository: OfficeMitraRepository
) : ViewModel() {

    val uiState: StateFlow<ApprovalsUiState> = combine(
        repository.currentUser,
        repository.approvalRequests
    ) { user, requests ->
        ApprovalsUiState(currentUser = user, approvalRequests = requests)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ApprovalsUiState(currentUser = repository.currentUser.value)
    )

    fun toggleApproverRole(isApprover: Boolean) {
        // Role is managed via company membership role on Supabase
    }

    fun updateApprovalStatus(requestId: String, isApproved: Boolean, comment: String? = null) {
        viewModelScope.launch {
            repository.updateApprovalStatus(requestId, isApproved, comment)
        }
    }
}
