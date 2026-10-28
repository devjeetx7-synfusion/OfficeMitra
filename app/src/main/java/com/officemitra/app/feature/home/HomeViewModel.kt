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
    val isRefreshing: Boolean = false,
    val recentActivities: List<AppNotification> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: OfficeMitraRepository
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)

    val uiState: StateFlow<HomeUiState> = combine(
        repository.currentUser,
        repository.isPunchedIn,
        repository.todayAttendance,
        repository.notifications,
        _isRefreshing
    ) { user, isPunchedIn, todayAttendance, notifications, isRefreshing ->
        HomeUiState(
            user = user,
            isPunchedIn = isPunchedIn,
            todayAttendance = todayAttendance,
            unreadNotificationCount = notifications.count { !it.isRead },
            isRefreshing = isRefreshing,
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
