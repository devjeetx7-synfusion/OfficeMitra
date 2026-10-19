package com.officemitra.app.feature.leave

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.officemitra.app.data.model.LeaveBalance
import com.officemitra.app.data.model.LeaveRequest
import com.officemitra.app.data.repository.DemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class LeaveUiState(
    val balance: LeaveBalance = LeaveBalance(),
    val requests: List<LeaveRequest> = emptyList()
)

@HiltViewModel
class LeaveViewModel @Inject constructor(
    private val repository: DemoRepository
) : ViewModel() {

    val uiState: StateFlow<LeaveUiState> = combine(
        repository.leaveBalance,
        repository.leaveRequests
    ) { balance, requests ->
        LeaveUiState(balance = balance, requests = requests)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        LeaveUiState()
    )

    fun applyLeave(leaveType: String, startDate: String, endDate: String, daysCount: Int, reason: String) {
        repository.applyLeave(leaveType, startDate, endDate, daysCount, reason)
    }

    fun cancelLeave(requestId: String) {
        repository.cancelLeaveRequest(requestId)
    }
}
