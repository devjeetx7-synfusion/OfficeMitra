package com.officemitra.app.data.repository

import com.officemitra.app.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoRepository @Inject constructor() {

    // Demo User
    private val _currentUser = MutableStateFlow(
        User(
            id = "USR-101",
            name = "Alex Morgan",
            email = "alex.morgan@officemitra.com",
            phone = "+1 555-019-2834",
            employeeId = "OM-EMP-2025",
            companyCode = "OM-10245",
            companyName = "Acme Technologies Corp",
            department = "Software Engineering",
            designation = "Senior Android Developer",
            joiningDate = "15 Jan 2022",
            isAuthorizedApprover = true,
            approvalStatus = ApprovalStatus.APPROVED
        )
    )
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    // Attendance State
    private val _isPunchedIn = MutableStateFlow(false)
    val isPunchedIn: StateFlow<Boolean> = _isPunchedIn.asStateFlow()

    private val _todayAttendance = MutableStateFlow(
        AttendanceRecord(
            id = "ATT-TODAY",
            date = getCurrentDateFormatted(),
            punchIn = "--:--",
            punchOut = "--:--",
            workingDuration = "0h 0m",
            status = "Not Punched"
        )
    )
    val todayAttendance: StateFlow<AttendanceRecord> = _todayAttendance.asStateFlow()

    private val _attendanceHistory = MutableStateFlow(
        listOf(
            AttendanceRecord("ATT-1", "01 May 2025", "09:00 AM", "06:15 PM", "9h 15m", "Present"),
            AttendanceRecord("ATT-2", "02 May 2025", "09:05 AM", "06:00 PM", "8h 55m", "Present"),
            AttendanceRecord("ATT-3", "03 May 2025", "09:12 AM", "06:30 PM", "9h 18m", "Present"),
            AttendanceRecord("ATT-4", "04 May 2025", "--:--", "--:--", "0h 0m", "Leave"),
            AttendanceRecord("ATT-5", "06 May 2025", "08:55 AM", "05:45 PM", "8h 50m", "Present"),
            AttendanceRecord("ATT-6", "07 May 2025", "09:30 AM", "02:30 PM", "5h 0m", "Half Day")
        )
    )
    val attendanceHistory: StateFlow<List<AttendanceRecord>> = _attendanceHistory.asStateFlow()

    // Leave State
    private val _leaveBalance = MutableStateFlow(LeaveBalance())
    val leaveBalance: StateFlow<LeaveBalance> = _leaveBalance.asStateFlow()

    private val _leaveRequests = MutableStateFlow(
        listOf(
            LeaveRequest("LR-101", "Casual Leave", "10 May 2025", "11 May 2025", 2, "Family Function", "Approved", "01 May 2025", "Approved by Manager"),
            LeaveRequest("LR-102", "Sick Leave", "18 May 2025", "18 May 2025", 1, "Doctor Appointment", "Pending", "05 May 2025"),
            LeaveRequest("LR-103", "Earned Leave", "25 May 2025", "28 May 2025", 4, "Vacation Trip", "Pending", "06 May 2025")
        )
    )
    val leaveRequests: StateFlow<List<LeaveRequest>> = _leaveRequests.asStateFlow()

    // Approval State
    private val _approvalRequests = MutableStateFlow(
        listOf(
            ApprovalRequest("AP-201", "Sarah Jenkins", "EMP-304", "UI/UX Design", "Leave Request", "06 May 2025", "Casual Leave: 12 May - 14 May (3 Days). Reason: Personal Work.", "Pending"),
            ApprovalRequest("AP-202", "David Chen", "EMP-211", "Backend Dev", "Attendance Correction", "05 May 2025", "Correction for 03 May: Forgot to punch out due to system update.", "Pending"),
            ApprovalRequest("AP-203", "Elena Rostova", "EMP-108", "QA Engineering", "Expense Claim", "02 May 2025", "Travel Allowance for Client Visit - $120.", "Approved", "Verified receipts.")
        )
    )
    val approvalRequests: StateFlow<List<ApprovalRequest>> = _approvalRequests.asStateFlow()

    // Holidays
    private val _holidays = MutableStateFlow(
        listOf(
            Holiday("HOL-1", "New Year's Day", "01 Jan 2025", "Wednesday", "Official Holiday", "January", "2025", false),
            Holiday("HOL-2", "Memorial Day", "26 May 2025", "Monday", "Federal Holiday", "May", "2025", true),
            Holiday("HOL-3", "Independence Day", "04 Jul 2025", "Friday", "National Holiday", "July", "2025", true),
            Holiday("HOL-4", "Labor Day", "01 Sep 2025", "Monday", "Federal Holiday", "September", "2025", true),
            Holiday("HOL-5", "Thanksgiving Day", "27 Nov 2025", "Thursday", "Federal Holiday", "November", "2025", true),
            Holiday("HOL-6", "Christmas Day", "25 Dec 2025", "Thursday", "Official Holiday", "December", "2025", true)
        )
    )
    val holidays: StateFlow<List<Holiday>> = _holidays.asStateFlow()

    // Salary Slips
    private val _salarySlips = MutableStateFlow(
        listOf(
            SalarySlip("SAL-2025-04", "April 2025", "01 Apr 2025 - 30 Apr 2025", "01 May 2025", "$4,500.00", "$3,920.00"),
            SalarySlip("SAL-2025-03", "March 2025", "01 Mar 2025 - 31 Mar 2025", "01 Apr 2025", "$4,500.00", "$3,920.00"),
            SalarySlip("SAL-2025-02", "February 2025", "01 Feb 2025 - 28 Feb 2025", "01 Mar 2025", "$4,500.00", "$3,920.00"),
            SalarySlip("SAL-2025-01", "January 2025", "01 Jan 2025 - 31 Jan 2025", "01 Feb 2025", "$4,500.00", "$3,920.00")
        )
    )
    val salarySlips: StateFlow<List<SalarySlip>> = _salarySlips.asStateFlow()

    // Notifications
    private val _notifications = MutableStateFlow(
        listOf(
            AppNotification("NOTI-1", "Leave Approved", "Your Casual Leave request for 10 May - 11 May has been approved.", "2 hours ago", "Leave", false, "leave"),
            AppNotification("NOTI-2", "Salary Slip Published", "Salary slip for April 2025 is now available for download.", "1 day ago", "Salary Slip", false, "salary"),
            AppNotification("NOTI-3", "Holiday Reminder", "Upcoming Memorial Day holiday on Monday, 26 May 2025.", "3 days ago", "Company Updates", true, "holidays"),
            AppNotification("NOTI-4", "Attendance Warning", "You punched in late on 07 May 2025 (09:30 AM).", "4 days ago", "Attendance", true, "attendance")
        )
    )
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    // Company Join Preview Demo Data
    val validCompanyCode = "OM-10245"
    val demoCompanyInfo = CompanyInfo(
        code = "OM-10245",
        name = "Acme Technologies Corp",
        address = "100 Innovation Way, Silicon Valley, CA",
        totalEmployees = 250
    )

    // Interactive Actions

    fun togglePunchInStatus(photoUrl: String? = null) {
        val currentTime = getCurrentTimeFormatted()
        val isCurrentlyPunched = _isPunchedIn.value

        if (!isCurrentlyPunched) {
            _isPunchedIn.value = true
            _todayAttendance.update {
                it.copy(
                    punchIn = currentTime,
                    status = "Present",
                    photoUrl = photoUrl
                )
            }
            addNotification("Punch In Successful", "Punched in at $currentTime.", "Attendance", "attendance")
        } else {
            _isPunchedIn.value = false
            _todayAttendance.update {
                it.copy(
                    punchOut = currentTime,
                    workingDuration = "8h 30m"
                )
            }
            val todayRecord = _todayAttendance.value
            _attendanceHistory.update { list ->
                listOf(todayRecord) + list.filter { it.id != todayRecord.id }
            }
            addNotification("Punch Out Successful", "Punched out at $currentTime.", "Attendance", "attendance")
        }
    }

    fun applyLeave(leaveType: String, startDate: String, endDate: String, daysCount: Int, reason: String) {
        val newRequest = LeaveRequest(
            id = "LR-${System.currentTimeMillis() % 10000}",
            leaveType = leaveType,
            startDate = startDate,
            endDate = endDate,
            daysCount = daysCount,
            reason = reason,
            status = "Pending",
            appliedDate = getCurrentDateFormatted()
        )
        _leaveRequests.update { listOf(newRequest) + it }
        addNotification("Leave Request Submitted", "Applied for $leaveType ($daysCount days).", "Leave", "leave")
    }

    fun cancelLeaveRequest(requestId: String) {
        _leaveRequests.update { list ->
            list.map { if (it.id == requestId) it.copy(status = "Cancelled") else it }
        }
    }

    fun submitAttendanceCorrection(date: String, requestedCorrection: String, reason: String) {
        val newApproval = ApprovalRequest(
            id = "AP-${System.currentTimeMillis() % 10000}",
            employeeName = _currentUser.value.name,
            employeeId = _currentUser.value.employeeId,
            department = _currentUser.value.department,
            requestType = "Attendance Correction",
            requestDate = getCurrentDateFormatted(),
            details = "Date: $date. Requested: $requestedCorrection. Reason: $reason",
            status = "Pending"
        )
        _approvalRequests.update { listOf(newApproval) + it }
        addNotification("Correction Submitted", "Attendance correction for $date submitted.", "Attendance", "attendance")
    }

    fun updateApprovalStatus(requestId: String, isApproved: Boolean, comment: String? = null) {
        val newStatus = if (isApproved) "Approved" else "Rejected"
        _approvalRequests.update { list ->
            list.map { if (it.id == requestId) it.copy(status = newStatus, decisionComment = comment) else it }
        }
    }

    fun markNotificationAsRead(id: String) {
        _notifications.update { list ->
            list.map { if (it.id == id) it.copy(isRead = true) else it }
        }
    }

    fun markAllNotificationsAsRead() {
        _notifications.update { list ->
            list.map { it.copy(isRead = true) }
        }
    }

    fun clearNotifications() {
        _notifications.value = emptyList()
    }

    fun updateProfile(name: String, email: String, phone: String) {
        _currentUser.update {
            it.copy(name = name, email = email, phone = phone)
        }
    }

    fun toggleAuthorizedApproverRole(isApprover: Boolean) {
        _currentUser.update {
            it.copy(isAuthorizedApprover = isApprover)
        }
    }

    private fun addNotification(title: String, message: String, category: String, route: String) {
        val noti = AppNotification(
            id = "NOTI-${System.currentTimeMillis() % 10000}",
            title = title,
            message = message,
            timestamp = "Just now",
            category = category,
            isRead = false,
            targetRoute = route
        )
        _notifications.update { listOf(noti) + it }
    }

    private fun getCurrentDateFormatted(): String {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun getCurrentTimeFormatted(): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date())
    }
}
