package com.officemitra.app.data.repository

import com.officemitra.app.data.model.*
import com.officemitra.app.data.remote.model.*
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseOfficeMitraRepository @Inject constructor(
    private val supabaseClient: SupabaseClient
) : OfficeMitraRepository {

    private val auth: Auth get() = supabaseClient.pluginManager.getPlugin(Auth)
    private val postgrest: Postgrest get() = supabaseClient.pluginManager.getPlugin(Postgrest)
    private val storage: Storage get() = supabaseClient.pluginManager.getPlugin(Storage)

    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    private val _currentUser = MutableStateFlow(
        User(
            id = "",
            name = "Employee",
            email = "",
            phone = "",
            employeeId = "",
            companyCode = "",
            companyName = "No Company Joined",
            department = "General",
            designation = "Employee",
            joiningDate = "N/A",
            isAuthorizedApprover = false,
            approvalStatus = ApprovalStatus.PENDING
        )
    )
    override val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    private val _isPunchedIn = MutableStateFlow(false)
    override val isPunchedIn: StateFlow<Boolean> = _isPunchedIn.asStateFlow()

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
    override val todayAttendance: StateFlow<AttendanceRecord> = _todayAttendance.asStateFlow()

    private val _attendanceHistory = MutableStateFlow<List<AttendanceRecord>>(emptyList())
    override val attendanceHistory: StateFlow<List<AttendanceRecord>> = _attendanceHistory.asStateFlow()

    private val _leaveBalance = MutableStateFlow(LeaveBalance())
    override val leaveBalance: StateFlow<LeaveBalance> = _leaveBalance.asStateFlow()

    private val _leaveRequests = MutableStateFlow<List<LeaveRequest>>(emptyList())
    override val leaveRequests: StateFlow<List<LeaveRequest>> = _leaveRequests.asStateFlow()

    private val _approvalRequests = MutableStateFlow<List<ApprovalRequest>>(emptyList())
    override val approvalRequests: StateFlow<List<ApprovalRequest>> = _approvalRequests.asStateFlow()

    private val _holidays = MutableStateFlow<List<Holiday>>(emptyList())
    override val holidays: StateFlow<List<Holiday>> = _holidays.asStateFlow()

    private val _salarySlips = MutableStateFlow<List<SalarySlip>>(emptyList())
    override val salarySlips: StateFlow<List<SalarySlip>> = _salarySlips.asStateFlow()

    private val _notifications = MutableStateFlow<List<AppNotification>>(emptyList())
    override val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    private var activeCompanyId: String? = null

    init {
        repositoryScope.launch {
            refreshAll()
        }
    }

    override suspend fun refreshAll() {
        val userId = auth.currentUserOrNull()?.id ?: return
        try {
            loadUserProfile()
            loadCompanyMembership(userId)
            loadTodayAttendance(userId)
            loadAttendanceHistory(userId)
            loadLeaveBalance(userId)
            loadLeaveRequests(userId)
            loadApprovalRequests(userId)
            loadHolidays()
            loadSalarySlips(userId)
            loadNotifications(userId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun loadUserProfile(): Result<User> {
        val userId = auth.currentUserOrNull()?.id ?: return Result.failure(IllegalStateException("User not authenticated"))
        return try {
            val profiles = postgrest["profiles"].select {
                filter {
                    eq("id", userId)
                }
            }.decodeList<ProfileDto>()

            val profile = profiles.firstOrNull() ?: return Result.failure(NoSuchElementException("Profile not found"))

            var updatedUser = _currentUser.value.copy(
                id = profile.id,
                name = profile.fullName,
                email = profile.email ?: auth.currentUserOrNull()?.email ?: "",
                phone = profile.phoneNumber ?: "",
                avatarUrl = profile.avatarPath
            )
            _currentUser.value = updatedUser
            Result.success(updatedUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun loadCompanyMembership(userId: String) {
        try {
            val memberships = postgrest["company_memberships"].select {
                filter {
                    eq("user_id", userId)
                }
            }.decodeList<CompanyMembershipDto>()

            val membership = memberships.firstOrNull()
            if (membership != null) {
                activeCompanyId = membership.companyId
                val companies = postgrest["companies"].select {
                    filter {
                        eq("id", membership.companyId)
                    }
                }.decodeList<CompanyDto>()

                val company = companies.firstOrNull()
                val companyName = company?.name ?: "Office Mitra"
                val companyCode = company?.code ?: ""

                val approvalStatus = when (membership.status.lowercase()) {
                    "approved" -> ApprovalStatus.APPROVED
                    "rejected" -> ApprovalStatus.REJECTED
                    else -> ApprovalStatus.PENDING
                }

                val isApprover = membership.role.lowercase() in listOf("manager", "hr", "admin")

                _currentUser.update {
                    it.copy(
                        employeeId = membership.employeeIdentifier,
                        companyCode = companyCode,
                        companyName = companyName,
                        department = membership.department,
                        designation = membership.jobTitle,
                        joiningDate = membership.joiningDate ?: "N/A",
                        isAuthorizedApprover = isApprover,
                        approvalStatus = approvalStatus
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun loadTodayAttendance(userId: String) {
        try {
            val todayDate = getCurrentIsoDate()
            val records = postgrest["attendance"].select {
                filter {
                    eq("user_id", userId)
                    eq("date", todayDate)
                }
            }.decodeList<AttendanceDto>()

            val record = records.firstOrNull()
            if (record != null) {
                val isPunched = record.punchIn != null && record.punchOut == null
                _isPunchedIn.value = isPunched
                _todayAttendance.value = AttendanceRecord(
                    id = record.id ?: "ATT-TODAY",
                    date = formatIsoDateToDisplay(record.date),
                    punchIn = formatTimestampToTime(record.punchIn),
                    punchOut = formatTimestampToTime(record.punchOut),
                    workingDuration = record.workingDuration,
                    status = record.status,
                    photoUrl = record.photoUrl,
                    location = record.location
                )
            } else {
                _isPunchedIn.value = false
                _todayAttendance.value = AttendanceRecord(
                    id = "ATT-TODAY",
                    date = getCurrentDateFormatted(),
                    punchIn = "--:--",
                    punchOut = "--:--",
                    workingDuration = "0h 0m",
                    status = "Not Punched"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun loadAttendanceHistory(userId: String) {
        try {
            val dtos = postgrest["attendance"].select {
                filter {
                    eq("user_id", userId)
                }
                order("date", Order.DESCENDING)
            }.decodeList<AttendanceDto>()

            val mapped = dtos.map { dto ->
                AttendanceRecord(
                    id = dto.id ?: "",
                    date = formatIsoDateToDisplay(dto.date),
                    punchIn = formatTimestampToTime(dto.punchIn),
                    punchOut = formatTimestampToTime(dto.punchOut),
                    workingDuration = dto.workingDuration,
                    status = dto.status,
                    photoUrl = dto.photoUrl,
                    location = dto.location
                )
            }
            _attendanceHistory.value = mapped
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun loadLeaveBalance(userId: String) {
        try {
            val dtos = postgrest["leave_balances"].select {
                filter {
                    eq("user_id", userId)
                }
            }.decodeList<LeaveBalanceDto>()

            val dto = dtos.firstOrNull()
            if (dto != null) {
                _leaveBalance.value = LeaveBalance(
                    casualLeaveTotal = dto.casualLeaveTotal,
                    casualLeaveUsed = dto.casualLeaveUsed,
                    sickLeaveTotal = dto.sickLeaveTotal,
                    sickLeaveUsed = dto.sickLeaveUsed,
                    earnedLeaveTotal = dto.earnedLeaveTotal,
                    earnedLeaveUsed = dto.earnedLeaveUsed
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun loadLeaveRequests(userId: String) {
        try {
            val dtos = postgrest["leave_requests"].select {
                filter {
                    eq("user_id", userId)
                }
                order("created_at", Order.DESCENDING)
            }.decodeList<LeaveRequestDto>()

            val mapped = dtos.map { dto ->
                LeaveRequest(
                    id = dto.id ?: "",
                    leaveType = dto.leaveType,
                    startDate = formatIsoDateToDisplay(dto.startDate),
                    endDate = formatIsoDateToDisplay(dto.endDate),
                    daysCount = dto.daysCount,
                    reason = dto.reason,
                    status = dto.status,
                    appliedDate = formatIsoTimestampToDisplayDate(dto.createdAt),
                    approverComment = dto.reviewerComment
                )
            }
            _leaveRequests.value = mapped
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun loadApprovalRequests(userId: String) {
        try {
            val companyId = activeCompanyId ?: return
            if (!_currentUser.value.isAuthorizedApprover) {
                _approvalRequests.value = emptyList()
                return
            }

            val dtos = postgrest["approval_requests"].select {
                filter {
                    eq("company_id", companyId)
                }
                order("created_at", Order.DESCENDING)
            }.decodeList<ApprovalRequestDto>()

            val mapped = dtos.map { dto ->
                ApprovalRequest(
                    id = dto.id ?: "",
                    employeeName = "Employee",
                    employeeId = "EMP",
                    department = "Department",
                    requestType = dto.requestType,
                    requestDate = formatIsoDateToDisplay(dto.requestDate),
                    details = dto.details,
                    status = dto.status,
                    decisionComment = dto.decisionComment
                )
            }
            _approvalRequests.value = mapped
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun loadHolidays() {
        try {
            val companyId = activeCompanyId ?: return
            val dtos = postgrest["holidays"].select {
                filter {
                    eq("company_id", companyId)
                }
                order("date", Order.ASCENDING)
            }.decodeList<HolidayDto>()

            val mapped = dtos.map { dto ->
                Holiday(
                    id = dto.id,
                    name = dto.name,
                    date = formatIsoDateToDisplay(dto.date),
                    dayOfWeek = dto.dayOfWeek,
                    description = dto.description ?: "",
                    month = dto.month,
                    year = dto.year,
                    isUpcoming = dto.isUpcoming
                )
            }
            _holidays.value = mapped
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun loadSalarySlips(userId: String) {
        try {
            val dtos = postgrest["salary_slips"].select {
                filter {
                    eq("user_id", userId)
                    eq("is_published", true)
                }
                order("publish_date", Order.DESCENDING)
            }.decodeList<SalarySlipDto>()

            val mapped = dtos.map { dto ->
                SalarySlip(
                    id = dto.id,
                    monthYear = dto.monthYear,
                    period = dto.period,
                    publishDate = formatIsoDateToDisplay(dto.publishDate),
                    basicSalary = dto.basicSalary,
                    netSalary = dto.netSalary,
                    pdfUrl = dto.pdfPath
                )
            }
            _salarySlips.value = mapped
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun loadNotifications(userId: String) {
        try {
            val dtos = postgrest["notifications"].select {
                filter {
                    eq("user_id", userId)
                }
                order("created_at", Order.DESCENDING)
            }.decodeList<NotificationDto>()

            val mapped = dtos.map { dto ->
                AppNotification(
                    id = dto.id,
                    title = dto.title,
                    message = dto.message,
                    timestamp = formatIsoTimestampToDisplayDate(dto.createdAt),
                    category = dto.category,
                    isRead = dto.isRead,
                    targetRoute = dto.targetRoute
                )
            }
            _notifications.value = mapped
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun updateProfile(name: String, email: String, phone: String): Result<Unit> {
        val userId = auth.currentUserOrNull()?.id ?: return Result.failure(IllegalStateException("Not authenticated"))
        return try {
            postgrest["profiles"].update(
                mapOf(
                    "full_name" to name,
                    "phone_number" to phone
                )
            ) {
                filter {
                    eq("id", userId)
                }
            }
            loadUserProfile()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun lookupCompanyByCode(companyCode: String): Result<CompanyInfo> {
        return try {
            val companies = postgrest["companies"].select {
                filter {
                    eq("code", companyCode.trim())
                }
            }.decodeList<CompanyDto>()

            val company = companies.firstOrNull() ?: return Result.failure(NoSuchElementException("Company code not found"))

            Result.success(
                CompanyInfo(
                    code = company.code,
                    name = company.name,
                    address = company.address ?: "",
                    totalEmployees = 1
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun joinCompanyByCode(companyCode: String, department: String, designation: String, employeeId: String): Result<Unit> {
        val userId = auth.currentUserOrNull()?.id ?: return Result.failure(IllegalStateException("Not authenticated"))
        return try {
            val companyResult = lookupCompanyByCode(companyCode)
            val companyInfo = companyResult.getOrThrow()

            val companies = postgrest["companies"].select {
                filter { eq("code", companyCode.trim()) }
            }.decodeList<CompanyDto>()

            val company = companies.first()

            val membership = CompanyMembershipDto(
                companyId = company.id,
                userId = userId,
                employeeIdentifier = employeeId.ifBlank { "EMP-${System.currentTimeMillis() % 10000}" },
                department = department.ifBlank { "General" },
                jobTitle = designation.ifBlank { "Employee" },
                status = "pending",
                role = "employee"
            )

            postgrest["company_memberships"].insert(membership)
            refreshAll()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createCompany(companyName: String, address: String): Result<Unit> {
        val userId = auth.currentUserOrNull()?.id ?: return Result.failure(IllegalStateException("Not authenticated"))
        return try {
            val companyCode = "OM-" + (10000..99999).random()
            val companyDto = CompanyDto(
                id = java.util.UUID.randomUUID().toString(),
                name = companyName,
                code = companyCode,
                address = address,
                ownerId = userId
            )

            postgrest["companies"].insert(companyDto)

            val membership = CompanyMembershipDto(
                companyId = companyDto.id,
                userId = userId,
                employeeIdentifier = "ADMIN-001",
                department = "Management",
                jobTitle = "Company Owner / Admin",
                status = "approved",
                role = "admin"
            )

            postgrest["company_memberships"].insert(membership)
            refreshAll()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun togglePunchInStatus(photoUrl: String?): Result<Unit> {
        val userId = auth.currentUserOrNull()?.id ?: return Result.failure(IllegalStateException("Not authenticated"))
        val companyId = activeCompanyId ?: return Result.failure(IllegalStateException("Please join a company first"))

        return try {
            val todayDate = getCurrentIsoDate()
            val isCurrentlyPunched = _isPunchedIn.value

            if (!isCurrentlyPunched) {
                val record = AttendanceDto(
                    companyId = companyId,
                    userId = userId,
                    date = todayDate,
                    punchIn = getCurrentIsoTimestamp(),
                    status = "Present",
                    photoUrl = photoUrl
                )
                postgrest["attendance"].insert(record)
            } else {
                val punchOut = getCurrentIsoTimestamp()
                val currentRecord = postgrest["attendance"].select {
                    filter {
                        eq("user_id", userId)
                        eq("date", todayDate)
                    }
                }.decodeList<AttendanceDto>().firstOrNull()
                    ?: return Result.failure(IllegalStateException("Today's attendance record was not found"))
                val punchInMillis = parseIsoTimestamp(currentRecord.punchIn)
                    ?: return Result.failure(IllegalStateException("Punch-in timestamp is invalid"))
                val punchOutMillis = System.currentTimeMillis()
                val totalMinutes = ((punchOutMillis - punchInMillis).coerceAtLeast(0L) / 60_000L)
                val workingDuration = "${totalMinutes / 60}h ${totalMinutes % 60}m"
                postgrest["attendance"].update(
                    mapOf(
                        "punch_out" to punchOut,
                        "working_duration" to workingDuration
                    )
                ) {
                    filter {
                        eq("user_id", userId)
                        eq("date", todayDate)
                    }
                }
            }

            loadTodayAttendance(userId)
            loadAttendanceHistory(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun applyLeave(leaveType: String, startDate: String, endDate: String, daysCount: Int, reason: String): Result<Unit> {
        val userId = auth.currentUserOrNull()?.id ?: return Result.failure(IllegalStateException("Not authenticated"))
        val companyId = activeCompanyId ?: return Result.failure(IllegalStateException("Please join a company first"))

        return try {
            val request = LeaveRequestDto(
                companyId = companyId,
                userId = userId,
                leaveType = leaveType,
                startDate = parseDisplayDateToIso(startDate),
                endDate = parseDisplayDateToIso(endDate),
                daysCount = daysCount,
                reason = reason,
                status = "Pending"
            )
            postgrest["leave_requests"].insert(request)

            val approval = ApprovalRequestDto(
                companyId = companyId,
                userId = userId,
                requestType = "Leave Request",
                requestDate = getCurrentIsoDate(),
                details = "$leaveType: $startDate - $endDate ($daysCount Days). Reason: $reason",
                status = "Pending"
            )
            postgrest["approval_requests"].insert(approval)

            loadLeaveRequests(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cancelLeaveRequest(requestId: String): Result<Unit> {
        return try {
            val userId = auth.currentUserOrNull()?.id
                ?: return Result.failure(IllegalStateException("Not authenticated"))
            postgrest["leave_requests"].update(mapOf("status" to "Cancelled")) {
                filter {
                    eq("id", requestId)
                    eq("user_id", userId)
                    eq("status", "Pending")
                }
            }
            auth.currentUserOrNull()?.id?.let { loadLeaveRequests(it) }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun submitAttendanceCorrection(date: String, requestedCorrection: String, reason: String): Result<Unit> {
        val userId = auth.currentUserOrNull()?.id ?: return Result.failure(IllegalStateException("Not authenticated"))
        val companyId = activeCompanyId ?: return Result.failure(IllegalStateException("Please join a company first"))

        return try {
            val approval = ApprovalRequestDto(
                companyId = companyId,
                userId = userId,
                requestType = "Attendance Correction",
                requestDate = getCurrentIsoDate(),
                details = "Date: $date. Correction: $requestedCorrection. Reason: $reason",
                status = "Pending"
            )
            postgrest["approval_requests"].insert(approval)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateApprovalStatus(requestId: String, isApproved: Boolean, comment: String?): Result<Unit> {
        val reviewerId = auth.currentUserOrNull()?.id
        val newStatus = if (isApproved) "Approved" else "Rejected"

        return try {
            postgrest["approval_requests"].update(
                mapOf(
                    "status" to newStatus,
                    "decision_comment" to (comment ?: ""),
                    "reviewer_id" to reviewerId
                )
            ) {
                filter {
                    eq("id", requestId)
                }
            }
            reviewerId?.let { loadApprovalRequests(it) }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markNotificationAsRead(id: String): Result<Unit> {
        return try {
            postgrest["notifications"].update(mapOf("is_read" to true)) {
                filter {
                    eq("id", id)
                }
            }
            auth.currentUserOrNull()?.id?.let { loadNotifications(it) }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markAllNotificationsAsRead(): Result<Unit> {
        val userId = auth.currentUserOrNull()?.id ?: return Result.failure(IllegalStateException("Not authenticated"))
        return try {
            postgrest["notifications"].update(mapOf("is_read" to true)) {
                filter {
                    eq("user_id", userId)
                }
            }
            loadNotifications(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun clearNotifications(): Result<Unit> {
        val userId = auth.currentUserOrNull()?.id ?: return Result.failure(IllegalStateException("Not authenticated"))
        return try {
            postgrest["notifications"].delete {
                filter {
                    eq("user_id", userId)
                }
            }
            _notifications.value = emptyList()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getCurrentIsoDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(Date())
    }

    private fun getCurrentIsoTimestamp(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")
        return sdf.format(Date())
    }

    private fun parseIsoTimestamp(value: String?): Long? {
        if (value.isNullOrBlank()) return null
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
            sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")
            sdf.parse(value.take(19))?.time
        } catch (_: Exception) {
            null
        }
    }

    private fun getCurrentDateFormatted(): String {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun formatIsoDateToDisplay(dateStr: String?): String {
        val nonNullStr = dateStr ?: return "N/A"
        if (nonNullStr.isBlank()) return "N/A"
        return try {
            val isoSdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val date = isoSdf.parse(nonNullStr.take(10)) ?: return nonNullStr
            val displaySdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            displaySdf.format(date)
        } catch (e: Exception) {
            nonNullStr
        }
    }

    private fun parseDisplayDateToIso(dateStr: String): String {
        return try {
            val displaySdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val date = displaySdf.parse(dateStr) ?: return getCurrentIsoDate()
            val isoSdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            isoSdf.format(date)
        } catch (e: Exception) {
            getCurrentIsoDate()
        }
    }

    private fun formatIsoTimestampToDisplayDate(timestamp: String?): String {
        val nonNullStr = timestamp ?: return "Just now"
        if (nonNullStr.isBlank()) return "Just now"
        return try {
            val isoSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
            val date = isoSdf.parse(nonNullStr.take(19)) ?: return "Recently"
            val displaySdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            displaySdf.format(date)
        } catch (e: Exception) {
            "Recently"
        }
    }

    private fun formatTimestampToTime(timestamp: String?): String {
        val nonNullStr = timestamp ?: return "--:--"
        if (nonNullStr.isBlank()) return "--:--"
        return try {
            val isoSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
            val date = isoSdf.parse(nonNullStr.take(19)) ?: return "--:--"
            val timeSdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
            timeSdf.format(date)
        } catch (e: Exception) {
            "--:--"
        }
    }

    private fun String?.isNull_or_blank(): Boolean = this == null || this.isBlank()
}
