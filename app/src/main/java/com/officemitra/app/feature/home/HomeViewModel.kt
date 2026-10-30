package com.officemitra.app.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.officemitra.app.data.model.*
import com.officemitra.app.data.repository.OfficeMitraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val user: User,
    val isPunchedIn: Boolean,
    val todayAttendance: AttendanceRecord,
    val unreadNotificationCount: Int,
    val presentDaysCount: Int = 0,
    val leaveDaysCount: Int = 0,
    val pendingLeaveCount: Int = 0,
    val isRefreshing: Boolean = false,
    val recentActivities: List<AppNotification> = emptyList()
)

private data class HomeCoreState(
    val user: User,
    val isPunchedIn: Boolean,
    val todayAttendance: AttendanceRecord,
    val isRefreshing: Boolean
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: OfficeMitraRepository
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)

    private val coreState: Flow<HomeCoreState> = combine(
        repository.currentUser,
        repository.isPunchedIn,
        repository.todayAttendance,
        _isRefreshing
    ) { user, isPunchedIn, todayAttendance, isRefreshing ->
        HomeCoreState(user, isPunchedIn, todayAttendance, isRefreshing)
    }

    val uiState: StateFlow<HomeUiState> = combine(
        coreState,
        repository.attendanceHistory,
        repository.leaveRequests,
        repository.notifications
    ) { core, history, leaveRequests, notifications ->
        val presentDays = history.count { it.status.equals("Present", ignoreCase = true) || (it.punchIn != "--:--" && it.punchIn.isNotBlank()) }
        val leaveDays = leaveRequests.filter { it.status.equals("Approved", ignoreCase = true) }.sumOf { it.daysCount }
        val pendingLeaves = leaveRequests.count { it.status.equals("Pending", ignoreCase = true) }

        HomeUiState(
            user = core.user,
            isPunchedIn = core.isPunchedIn,
            todayAttendance = core.todayAttendance,
            unreadNotificationCount = notifications.count { !it.isRead },
            presentDaysCount = presentDays,
            leaveDaysCount = leaveDays,
            pendingLeaveCount = pendingLeaves,
            isRefreshing = core.isRefreshing,
            recentActivities = notifications.take(3)
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        HomeUiState(
            user = repository.currentUser.value,
            isPunchedIn = repository.isPunchedIn.value,
            todayAttendance = repository.todayAttendance.value,
            unreadNotificationCount = 0
        )
    )

    fun togglePunchStatus() {
        viewModelScope.launch {
            repository.togglePunchInStatus()
        }
    }

    fun refreshDashboard() {
        viewModelScope.launch {
            _isRefreshing.value = true
            repository.refreshAll()
            _isRefreshing.value = false
        }
    }
}
