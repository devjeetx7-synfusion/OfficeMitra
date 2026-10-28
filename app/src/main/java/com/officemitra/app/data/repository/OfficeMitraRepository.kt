package com.officemitra.app.data.repository

import com.officemitra.app.data.model.*
import kotlinx.coroutines.flow.StateFlow

interface OfficeMitraRepository {
    val currentUser: StateFlow<User>
    val isPunchedIn: StateFlow<Boolean>
    val todayAttendance: StateFlow<AttendanceRecord>
    val attendanceHistory: StateFlow<List<AttendanceRecord>>
    val leaveBalance: StateFlow<LeaveBalance>
    val leaveRequests: StateFlow<List<LeaveRequest>>
    val approvalRequests: StateFlow<List<ApprovalRequest>>
    val holidays: StateFlow<List<Holiday>>
    val salarySlips: StateFlow<List<SalarySlip>>
    val notifications: StateFlow<List<AppNotification>>

    suspend fun refreshAll()
    suspend fun loadUserProfile(): Result<User>
    suspend fun updateProfile(name: String, email: String, phone: String): Result<Unit>
    suspend fun joinCompanyByCode(companyCode: String, department: String, designation: String, employeeId: String): Result<Unit>
    suspend fun createCompany(companyName: String, address: String): Result<Unit>
    suspend fun togglePunchInStatus(photoUrl: String? = null): Result<Unit>
    suspend fun applyLeave(leaveType: String, startDate: String, endDate: String, daysCount: Int, reason: String): Result<Unit>
    suspend fun cancelLeaveRequest(requestId: String): Result<Unit>
    suspend fun submitAttendanceCorrection(date: String, requestedCorrection: String, reason: String): Result<Unit>
    suspend fun updateApprovalStatus(requestId: String, isApproved: Boolean, comment: String? = null): Result<Unit>
    suspend fun markNotificationAsRead(id: String): Result<Unit>
    suspend fun markAllNotificationsAsRead(): Result<Unit>
    suspend fun clearNotifications(): Result<Unit>
    suspend fun lookupCompanyByCode(companyCode: String): Result<CompanyInfo>
}
