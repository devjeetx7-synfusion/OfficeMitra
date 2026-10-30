package com.officemitra.app.data.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val employeeId: String = "",
    val companyCode: String = "",
    val companyName: String = "",
    val department: String = "",
    val designation: String = "",
    val avatarUrl: String? = null,
    val joiningDate: String = "",
    val isAuthorizedApprover: Boolean = false,
    val approvalStatus: ApprovalStatus = ApprovalStatus.PENDING
)

enum class ApprovalStatus {
    PENDING,
    APPROVED,
    REJECTED
}

data class CompanyInfo(
    val code: String,
    val name: String,
    val address: String,
    val totalEmployees: Int
)

data class AttendanceRecord(
    val id: String = "",
    val date: String = "",
    val punchIn: String = "--:--",
    val punchOut: String = "--:--",
    val workingDuration: String = "0h 0m",
    val status: String = "Not Punched",
    val photoUrl: String? = null,
    val location: String = "Office HQ"
)

data class LeaveBalance(
    val casualLeaveTotal: Int = 0,
    val casualLeaveUsed: Int = 0,
    val sickLeaveTotal: Int = 0,
    val sickLeaveUsed: Int = 0,
    val earnedLeaveTotal: Int = 0,
    val earnedLeaveUsed: Int = 0
) {
    val totalLeave: Int get() = casualLeaveTotal + sickLeaveTotal + earnedLeaveTotal
    val totalUsed: Int get() = casualLeaveUsed + sickLeaveUsed + earnedLeaveUsed
    val totalRemaining: Int get() = (totalLeave - totalUsed).coerceAtLeast(0)
}

data class LeaveRequest(
    val id: String,
    val leaveType: String,
    val startDate: String,
    val endDate: String,
    val daysCount: Int,
    val reason: String,
    val status: String,
    val appliedDate: String,
    val approverComment: String? = null
)

data class ApprovalRequest(
    val id: String,
    val employeeName: String,
    val employeeId: String,
    val department: String,
    val requestType: String,
    val requestDate: String,
    val details: String,
    val status: String,
    val decisionComment: String? = null
)

data class Holiday(
    val id: String,
    val name: String,
    val date: String,
    val dayOfWeek: String,
    val description: String,
    val month: String,
    val year: String,
    val isUpcoming: Boolean = true
)

data class SalarySlip(
    val id: String,
    val monthYear: String,
    val period: String,
    val publishDate: String,
    val basicSalary: String,
    val netSalary: String,
    val pdfUrl: String = ""
)

data class AppNotification(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val category: String,
    val isRead: Boolean = false,
    val targetRoute: String? = null
)
