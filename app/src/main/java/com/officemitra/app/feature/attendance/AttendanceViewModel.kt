package com.officemitra.app.feature.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.officemitra.app.data.model.AttendanceRecord
import com.officemitra.app.data.repository.OfficeMitraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AttendanceUiState(
    val isPunchedIn: Boolean = false,
    val todayAttendance: AttendanceRecord,
    val historyRecords: List<AttendanceRecord> = emptyList(),
    val locationStatus: String = "Office HQ",
    val isCameraCaptured: Boolean = false,
    val capturedPhotoPath: String? = null
)

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val repository: OfficeMitraRepository
) : ViewModel() {

    val uiState: StateFlow<AttendanceUiState> = combine(
        repository.isPunchedIn,
        repository.todayAttendance,
        repository.attendanceHistory
    ) { isPunchedIn, todayAttendance, history ->
        AttendanceUiState(
            isPunchedIn = isPunchedIn,
            todayAttendance = todayAttendance,
            historyRecords = history
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AttendanceUiState(
            todayAttendance = repository.todayAttendance.value
        )
    )

    fun punchInOrOut() {
        viewModelScope.launch {
            repository.togglePunchInStatus()
        }
    }

    fun submitCorrection(date: String, requestedTime: String, reason: String) {
        viewModelScope.launch {
            repository.submitAttendanceCorrection(date, requestedTime, reason)
        }
    }
}
