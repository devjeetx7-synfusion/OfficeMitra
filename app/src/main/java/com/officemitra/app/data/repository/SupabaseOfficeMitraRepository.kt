package com.officemitra.app.data.repository

import android.util.Log
import com.officemitra.app.data.auth.AuthAction
import com.officemitra.app.data.auth.AuthErrorMapper
import com.officemitra.app.data.auth.AuthErrorType
import com.officemitra.app.data.auth.AuthValidator
import com.officemitra.app.data.model.*
import com.officemitra.app.data.remote.model.*
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.NotFoundRestException
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.put
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseOfficeMitraRepository @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val authRepository: AuthRepository
) : OfficeMitraRepository {

    private val auth: Auth get() = supabaseClient.pluginManager.getPlugin(Auth)
    private val postgrest: Postgrest get() = supabaseClient.pluginManager.getPlugin(Postgrest)
    private val storage: Storage get() = supabaseClient.pluginManager.getPlugin(Storage)

    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _currentUser = MutableStateFlow(User())
    override val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    private val _accountState = MutableStateFlow(AccountState())
    override val accountState: StateFlow<AccountState> = _accountState.asStateFlow()

    /** Serialises refreshes and clears so that data of two users can never be mixed. */
    private val refreshMutex = Mutex()

    /** The user whose data is currently cached. */
    private var loadedUserId: String? = null

    private val _isPunchedIn = MutableStateFlow(false)
    override val isPunchedIn: StateFlow<Boolean> = _isPunchedIn.asStateFlow()

    private val _todayAttendance = MutableStateFlow(
        AttendanceRecord(
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
        // This repository is a singleton that outlives every login. The cached data must therefore
        // follow the session: it is dropped as soon as the session ends, so the next user can never
        // see what the previous one loaded.
        repositoryScope.launch {
            authRepository.authState.collect { state ->
                if (state is AuthState.SignedOut) clearSessionData()
            }
        }
    }

    override suspend fun refreshAll() {
        refreshMutex.withLock {
            val userId = auth.currentUserOrNull()?.id
            if (userId == null) {
                resetState()
                return@withLock
            }
            if (loadedUserId != userId) {
                resetState()
                loadedUserId = userId
            }

            val profileError = loadUserProfile().exceptionOrNull()
            val membershipError = loadCompanyMembership(userId)
            val failure = membershipError ?: profileError
            _accountState.update {
                it.copy(
                    membership = if (failure != null) MembershipState.UNKNOWN else it.membership,
                    loadError = failure.toLoadError()
                )
            }

            loadTodayAttendance(userId)
            loadAttendanceHistory(userId)
            loadLeaveBalance(userId)
            loadLeaveRequests(userId)
            loadApprovalRequests(userId)
            loadHolidays()
            loadSalarySlips(userId)
            loadNotifications(userId)

            // The session may have ended or changed while the requests were running.
            if (auth.currentUserOrNull()?.id != userId) resetState()
        }
    }

    override suspend fun clearSessionData() {
        refreshMutex.withLock { resetState() }
    }

    private fun resetState() {
        loadedUserId = null
        activeCompanyId = null
        _currentUser.value = User()
        _accountState.value = AccountState()
        _isPunchedIn.value = false
        _todayAttendance.value = AttendanceRecord(date = getCurrentDateFormatted())
        _attendanceHistory.value = emptyList()
        _leaveBalance.value = LeaveBalance()
        _leaveRequests.value = emptyList()
        _approvalRequests.value = emptyList()
        _holidays.value = emptyList()
        _salarySlips.value = emptyList()
        _notifications.value = emptyList()
    }

    private fun Throwable?.toLoadError(): AccountLoadError = when {
        this == null -> AccountLoadError.NONE
        AuthErrorMapper.classify(this) in setOf(AuthErrorType.NETWORK, AuthErrorType.TIMEOUT) -> AccountLoadError.NETWORK
        else -> AccountLoadError.OTHER
    }

    /** Logs only the failing step and the exception type: REST exception messages contain the bearer token. */
    private fun logFailure(step: String, error: Throwable) {
        if (error is CancellationException) return
        Log.w(TAG, "$step failed: ${error::class.simpleName} (${AuthErrorMapper.classify(error)})")
    }

    private fun JsonObject?.stringOrNull(key: String): String? =
        (this?.get(key) as? JsonPrimitive)?.contentOrNull?.trim()?.takeIf { it.isNotEmpty() }

    private suspend fun fetchProfile(userId: String): ProfileDto? =
        postgrest["profiles"].select {
            filter {
                eq("id", userId)
            }
        }.decodeList<ProfileDto>().firstOrNull()

    override suspend fun loadUserProfile(): Result<User> {
        val authUser = auth.currentUserOrNull() ?: return Result.failure(AuthErrorMapper.notSignedIn())
        val userId = authUser.id
        val metadataName = authUser.userMetadata.stringOrNull("full_name")
            ?: authUser.email?.substringBefore("@")
            ?: "Employee"
        val metadataPhone = authUser.userMetadata.stringOrNull("phone_number") ?: ""

        return try {
            var profile = fetchProfile(userId)
            if (profile == null) {
                // The row is normally created by the database trigger when the account is created.
                // This is only a fallback. The primary key makes a second row impossible and
                // ignoreDuplicates makes a race with the trigger harmless.
                try {
                    postgrest["profiles"].upsert(
                        ProfileInsertDto(id = userId, fullName = metadataName, phoneNumber = metadataPhone),
                        onConflict = "id",
                        ignoreDuplicates = true
                    )
                    profile = fetchProfile(userId)
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    logFailure("createProfile", e)
                }
            }

            if (auth.currentUserOrNull()?.id != userId) {
                return Result.failure(AuthErrorMapper.notSignedIn())
            }

            val loaded: ProfileDto? = profile
            val updatedUser = _currentUser.value.copy(
                id = userId,
                name = loaded?.fullName?.takeIf { it.isNotBlank() } ?: metadataName,
                // The auth account owns the email address; the profile column is only a copy.
                email = authUser.email ?: loaded?.email ?: "",
                phone = loaded?.phoneNumber ?: metadataPhone,
                avatarUrl = loaded?.avatarPath
            )
            _currentUser.value = updatedUser
            val suspended = loaded != null && !loaded.accountStatus.equals("active", ignoreCase = true)
            _accountState.update { it.copy(isSuspended = suspended) }
            Result.success(updatedUser)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            logFailure("loadUserProfile", e)
            Result.failure(AuthErrorMapper.toSafeException(e, AuthAction.PROFILE))
        }
    }

    /** Returns the failure, if any, so that the caller can tell "no company" from "could not load". */
    private suspend fun loadCompanyMembership(userId: String): Throwable? {
        return try {
            val memberships = postgrest["company_memberships"].select {
                filter {
                    eq("user_id", userId)
                }
            }.decodeList<CompanyMembershipDto>()

            val state = MembershipState.fromStatuses(memberships.map { it.status })
            // Act on the membership that decided the state, not on whichever row came back first.
            val membership = when (state) {
                MembershipState.APPROVED -> memberships.firstOrNull { it.status.trim().equals("approved", ignoreCase = true) }
                MembershipState.PENDING -> memberships.firstOrNull { it.status.trim().equals("pending", ignoreCase = true) }
                else -> memberships.firstOrNull()
            }

            if (membership != null) {
                activeCompanyId = if (state == MembershipState.APPROVED) membership.companyId else null
                val company = try {
                    postgrest["companies"].select {
                        filter {
                            eq("id", membership.companyId)
                        }
                    }.decodeList<CompanyDto>().firstOrNull()
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    // The company name is only decoration here; the membership itself was loaded.
                    logFailure("loadCompany", e)
                    null
                }
                val companyName = company?.name ?: ""
                val companyCode = company?.code ?: ""

                val approvalStatus = when (state) {
                    MembershipState.APPROVED -> ApprovalStatus.APPROVED
                    MembershipState.PENDING -> ApprovalStatus.PENDING
                    else -> ApprovalStatus.REJECTED
                }

                val isApprover = state == MembershipState.APPROVED &&
                    membership.role.lowercase() in listOf("manager", "hr", "admin")

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
            } else {
                activeCompanyId = null
                _currentUser.update {
                    it.copy(
                        employeeId = "",
                        companyCode = "",
                        companyName = "",
                        department = "",
                        designation = "",
                        joiningDate = "",
                        isAuthorizedApprover = false,
                        approvalStatus = ApprovalStatus.PENDING
                    )
                }
            }
            _accountState.update { it.copy(membership = state) }
            null
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            logFailure("loadCompanyMembership", e)
            e
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
                    id = "",
                    date = getCurrentDateFormatted(),
                    punchIn = "--:--",
                    punchOut = "--:--",
                    workingDuration = "0h 0m",
                    status = "Not Punched"
                )
            }
        } catch (e: Exception) {
            logFailure("refresh", e)
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
            logFailure("refresh", e)
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
            } else {
                _leaveBalance.value = LeaveBalance()
            }
        } catch (e: Exception) {
            logFailure("refresh", e)
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
            logFailure("refresh", e)
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
                var name = "Applicant"
                var empId = ""
                var dept = ""
                try {
                    val applicantProfiles = postgrest["profiles"].select {
                        filter { eq("id", dto.userId) }
                    }.decodeList<ProfileDto>()
                    applicantProfiles.firstOrNull()?.let { p -> name = p.fullName }

                    val applicantMemberships = postgrest["company_memberships"].select {
                        filter {
                            eq("company_id", companyId)
                            eq("user_id", dto.userId)
                        }
                    }.decodeList<CompanyMembershipDto>()
                    applicantMemberships.firstOrNull()?.let { m ->
                        empId = m.employeeIdentifier
                        dept = m.department
                    }
                } catch (_: Exception) {}

                ApprovalRequest(
                    id = dto.id ?: "",
                    employeeName = name,
                    employeeId = empId,
                    department = dept,
                    requestType = dto.requestType,
                    requestDate = formatIsoDateToDisplay(dto.requestDate),
                    details = dto.details,
                    status = dto.status,
                    decisionComment = dto.decisionComment
                )
            }
            _approvalRequests.value = mapped
        } catch (e: Exception) {
            logFailure("refresh", e)
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
            logFailure("refresh", e)
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
            logFailure("refresh", e)
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
            logFailure("refresh", e)
        }
    }

    override suspend fun updateProfile(name: String, email: String, phone: String): Result<Unit> {
        val userId = auth.currentUserOrNull()?.id ?: return Result.failure(AuthErrorMapper.notSignedIn())
        AuthValidator.fullNameError(name)?.let { return Result.failure(AuthErrorMapper.appError(it)) }
        AuthValidator.phoneError(phone)?.let { return Result.failure(AuthErrorMapper.appError(it)) }
        return try {
            // The row is always addressed by the id of the current session and row level security
            // enforces the same on the server, so another user's profile can never be written.
            val updated = postgrest["profiles"].update(
                ProfileUpdateDto(fullName = name.trim(), phoneNumber = AuthValidator.normalizePhone(phone))
            ) {
                filter {
                    eq("id", userId)
                }
                select()
            }.decodeList<ProfileDto>()

            val profile = updated.firstOrNull()
                ?: return Result.failure(AuthErrorMapper.appError("Your profile could not be found. Please log out and log in again."))

            if (auth.currentUserOrNull()?.id == userId) {
                _currentUser.update {
                    it.copy(name = profile.fullName, phone = profile.phoneNumber ?: "", avatarUrl = profile.avatarPath)
                }
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            logFailure("updateProfile", e)
            Result.failure(AuthErrorMapper.toSafeException(e, AuthAction.PROFILE))
        }
    }

    /**
     * Finds an active company by its join code.
     *
     * Uses the `lookup_company_by_code` database function, which returns only the matching company.
     * Databases that do not have migration 00002 yet do not have the function; those still allow a
     * direct query, so the lookup falls back to it.
     */
    private suspend fun findCompanyByCode(code: String): CompanyDto? {
        val viaFunction = try {
            postgrest.rpc("lookup_company_by_code", buildJsonObject { put("p_code", code) })
                .decodeList<CompanyDto>()
        } catch (e: NotFoundRestException) {
            null
        }
        if (viaFunction != null) return viaFunction.firstOrNull()

        return postgrest["companies"].select {
            filter {
                eq("code", code)
            }
        }.decodeList<CompanyDto>().firstOrNull()
    }

    override suspend fun lookupCompanyByCode(companyCode: String): Result<CompanyInfo> {
        val cleanCode = companyCode.trim()
        if (cleanCode.isBlank()) return Result.failure(AuthErrorMapper.appError("Company code cannot be empty"))
        if (auth.currentUserOrNull() == null) return Result.failure(AuthErrorMapper.notSignedIn())

        return try {
            val company = findCompanyByCode(cleanCode)
                ?: return Result.failure(AuthErrorMapper.appError("Company code not found"))

            Result.success(
                CompanyInfo(
                    code = company.code,
                    name = company.name,
                    address = company.address ?: "",
                    totalEmployees = 1
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            logFailure("lookupCompanyByCode", e)
            Result.failure(AuthErrorMapper.toSafeException(e))
        }
    }

    override suspend fun joinCompanyByCode(companyCode: String, department: String, designation: String, employeeId: String): Result<Unit> {
        val userId = auth.currentUserOrNull()?.id ?: return Result.failure(AuthErrorMapper.notSignedIn())
        val cleanCode = companyCode.trim()
        if (cleanCode.isBlank()) return Result.failure(AuthErrorMapper.appError("Company code cannot be empty"))
        return try {
            val company = findCompanyByCode(cleanCode)
                ?: return Result.failure(AuthErrorMapper.appError("Company code not found"))

            // A join request is always a pending employee membership for the signed-in user. The
            // database rejects anything else, so approval cannot be granted from the app.
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
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            logFailure("joinCompanyByCode", e)
            if (AuthErrorMapper.classify(e) == AuthErrorType.CONFLICT) {
                Result.failure(AuthErrorMapper.appError("You have already asked to join this company."))
            } else {
                Result.failure(AuthErrorMapper.toSafeException(e))
            }
        }
    }

    override suspend fun createCompany(companyName: String, address: String): Result<Unit> {
        val userId = auth.currentUserOrNull()?.id ?: return Result.failure(AuthErrorMapper.notSignedIn())
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
                employeeIdentifier = "ADMIN-${System.currentTimeMillis() % 1000}",
                department = "Management",
                jobTitle = "Company Owner / Admin",
                status = "approved",
                role = "admin"
            )

            postgrest["company_memberships"].insert(membership)
            refreshAll()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(AuthErrorMapper.toSafeException(e))
        }
    }

    override suspend fun togglePunchInStatus(photoUrl: String?): Result<Unit> {
        val userId = auth.currentUserOrNull()?.id ?: return Result.failure(AuthErrorMapper.notSignedIn())
        val companyId = activeCompanyId ?: return Result.failure(AuthErrorMapper.appError("Please join a company first"))

        if (_currentUser.value.approvalStatus != ApprovalStatus.APPROVED) {
            return Result.failure(AuthErrorMapper.appError("Your company membership is awaiting approval."))
        }

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
                    ?: return Result.failure(AuthErrorMapper.appError("Today's attendance record was not found"))
                val punchInMillis = parseIsoTimestamp(currentRecord.punchIn)
                    ?: return Result.failure(AuthErrorMapper.appError("Punch-in timestamp is invalid"))
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
            Result.failure(AuthErrorMapper.toSafeException(e))
        }
    }

    override suspend fun applyLeave(leaveType: String, startDate: String, endDate: String, daysCount: Int, reason: String): Result<Unit> {
        val userId = auth.currentUserOrNull()?.id ?: return Result.failure(AuthErrorMapper.notSignedIn())
        val companyId = activeCompanyId ?: return Result.failure(AuthErrorMapper.appError("Please join a company first"))

        if (_currentUser.value.approvalStatus != ApprovalStatus.APPROVED) {
            return Result.failure(AuthErrorMapper.appError("Your company membership is awaiting approval."))
        }

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
            Result.failure(AuthErrorMapper.toSafeException(e))
        }
    }

    override suspend fun cancelLeaveRequest(requestId: String): Result<Unit> {
        return try {
            val userId = auth.currentUserOrNull()?.id
                ?: return Result.failure(AuthErrorMapper.notSignedIn())
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
            Result.failure(AuthErrorMapper.toSafeException(e))
        }
    }

    override suspend fun submitAttendanceCorrection(date: String, requestedCorrection: String, reason: String): Result<Unit> {
        val userId = auth.currentUserOrNull()?.id ?: return Result.failure(AuthErrorMapper.notSignedIn())
        val companyId = activeCompanyId ?: return Result.failure(AuthErrorMapper.appError("Please join a company first"))

        if (_currentUser.value.approvalStatus != ApprovalStatus.APPROVED) {
            return Result.failure(AuthErrorMapper.appError("Your company membership is awaiting approval."))
        }

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
            Result.failure(AuthErrorMapper.toSafeException(e))
        }
    }

    override suspend fun updateApprovalStatus(requestId: String, isApproved: Boolean, comment: String?): Result<Unit> {
        val reviewerId = auth.currentUserOrNull()?.id
        if (!_currentUser.value.isAuthorizedApprover) {
            return Result.failure(AuthErrorMapper.appError("Only authorized company managers/admins can review requests."))
        }

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
            Result.failure(AuthErrorMapper.toSafeException(e))
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
            Result.failure(AuthErrorMapper.toSafeException(e))
        }
    }

    override suspend fun markAllNotificationsAsRead(): Result<Unit> {
        val userId = auth.currentUserOrNull()?.id ?: return Result.failure(AuthErrorMapper.notSignedIn())
        return try {
            postgrest["notifications"].update(mapOf("is_read" to true)) {
                filter {
                    eq("user_id", userId)
                }
            }
            loadNotifications(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(AuthErrorMapper.toSafeException(e))
        }
    }

    override suspend fun clearNotifications(): Result<Unit> {
        val userId = auth.currentUserOrNull()?.id ?: return Result.failure(AuthErrorMapper.notSignedIn())
        return try {
            postgrest["notifications"].delete {
                filter {
                    eq("user_id", userId)
                }
            }
            _notifications.value = emptyList()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(AuthErrorMapper.toSafeException(e))
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

    private companion object {
        const val TAG = "OfficeMitraRepository"
    }
}
