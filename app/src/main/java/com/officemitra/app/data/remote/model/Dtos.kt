package com.officemitra.app.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    @SerialName("id") val id: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("email") val email: String? = null,
    @SerialName("phone_number") val phoneNumber: String? = null,
    @SerialName("avatar_path") val avatarPath: String? = null,
    @SerialName("account_status") val accountStatus: String = "active",
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
)

@Serializable
data class CompanyDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("code") val code: String,
    @SerialName("address") val address: String? = null,
    @SerialName("owner_id") val ownerId: String? = null,
    @SerialName("status") val status: String = "active",
    @SerialName("created_at") val createdAt: String? = null
)

@Serializable
data class CompanyMembershipDto(
    @SerialName("id") val id: String? = null,
    @SerialName("company_id") val companyId: String,
    @SerialName("user_id") val userId: String,
    @SerialName("employee_identifier") val employeeIdentifier: String,
    @SerialName("department") val department: String,
    @SerialName("job_title") val jobTitle: String,
    @SerialName("status") val status: String = "approved",
    @SerialName("role") val role: String = "employee",
    @SerialName("joining_date") val joiningDate: String? = null
)

@Serializable
data class AttendanceDto(
    @SerialName("id") val id: String? = null,
    @SerialName("company_id") val companyId: String,
    @SerialName("user_id") val userId: String,
    @SerialName("date") val date: String,
    @SerialName("punch_in") val punchIn: String? = null,
    @SerialName("punch_out") val punchOut: String? = null,
    @SerialName("status") val status: String = "Present",
    @SerialName("working_duration") val workingDuration: String = "0h 0m",
    @SerialName("notes") val notes: String? = null,
    @SerialName("photo_url") val photoUrl: String? = null,
    @SerialName("location") val location: String = "Office HQ"
)

@Serializable
data class LeaveBalanceDto(
    @SerialName("id") val id: String? = null,
    @SerialName("company_id") val companyId: String,
    @SerialName("user_id") val userId: String,
    @SerialName("casual_leave_total") val casualLeaveTotal: Int = 12,
    @SerialName("casual_leave_used") val casualLeaveUsed: Int = 0,
    @SerialName("sick_leave_total") val sickLeaveTotal: Int = 10,
    @SerialName("sick_leave_used") val sickLeaveUsed: Int = 0,
    @SerialName("earned_leave_total") val earnedLeaveTotal: Int = 15,
    @SerialName("earned_leave_used") val earnedLeaveUsed: Int = 0
)

@Serializable
data class LeaveRequestDto(
    @SerialName("id") val id: String? = null,
    @SerialName("company_id") val companyId: String,
    @SerialName("user_id") val userId: String,
    @SerialName("leave_type") val leaveType: String,
    @SerialName("start_date") val startDate: String,
    @SerialName("end_date") val endDate: String,
    @SerialName("days_count") val daysCount: Int = 1,
    @SerialName("reason") val reason: String,
    @SerialName("status") val status: String = "Pending",
    @SerialName("reviewer_id") val reviewerId: String? = null,
    @SerialName("reviewer_comment") val reviewerComment: String? = null,
    @SerialName("created_at") val createdAt: String? = null
)

@Serializable
data class ApprovalRequestDto(
    @SerialName("id") val id: String? = null,
    @SerialName("company_id") val companyId: String,
    @SerialName("user_id") val userId: String,
    @SerialName("request_type") val requestType: String,
    @SerialName("request_date") val requestDate: String,
    @SerialName("details") val details: String,
    @SerialName("status") val status: String = "Pending",
    @SerialName("decision_comment") val decisionComment: String? = null,
    @SerialName("reviewer_id") val reviewerId: String? = null,
    @SerialName("created_at") val createdAt: String? = null
)

@Serializable
data class HolidayDto(
    @SerialName("id") val id: String,
    @SerialName("company_id") val companyId: String,
    @SerialName("name") val name: String,
    @SerialName("date") val date: String,
    @SerialName("day_of_week") val dayOfWeek: String,
    @SerialName("description") val description: String? = null,
    @SerialName("month") val month: String,
    @SerialName("year") val year: String,
    @SerialName("is_upcoming") val isUpcoming: Boolean = true
)

@Serializable
data class SalarySlipDto(
    @SerialName("id") val id: String,
    @SerialName("company_id") val companyId: String,
    @SerialName("user_id") val userId: String,
    @SerialName("month_year") val monthYear: String,
    @SerialName("period") val period: String,
    @SerialName("publish_date") val publishDate: String,
    @SerialName("basic_salary") val basicSalary: String,
    @SerialName("net_salary") val netSalary: String,
    @SerialName("pdf_path") val pdfPath: String,
    @SerialName("is_published") val isPublished: Boolean = true
)

@Serializable
data class NotificationDto(
    @SerialName("id") val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("company_id") val companyId: String? = null,
    @SerialName("title") val title: String,
    @SerialName("message") val message: String,
    @SerialName("category") val category: String,
    @SerialName("is_read") val isRead: Boolean = false,
    @SerialName("target_route") val targetRoute: String? = null,
    @SerialName("created_at") val createdAt: String? = null
)
