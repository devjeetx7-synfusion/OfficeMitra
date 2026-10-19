package com.officemitra.app.data.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val employeeId: String,
    val companyCode: String,
    val companyName: String,
    val department: String,
    val designation: String,
    val avatarUrl: String? = null,
    val joiningDate: String,
    val isAuthorizedApprover: Boolean = true,
    val approvalStatus: ApprovalStatus = ApprovalStatus.APPROVED
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
    val id: String,
    val date: String,
    val punchIn: String,
    val punchOut: String,
    val workingDuration: String,
    val status: String, // "Present", "Absent", "Leave", "Half Day"
    val photoUrl: String? = null,
    val location: String = "Office HQ"
)

data class LeaveBalance(
    val casualLeaveTotal: Int = 12,
    val casualLeaveUsed: Int = 3,
    val sickLeaveTotal: Int = 10,
    val sickLeaveUsed: Int = 2,
    val earnedLeaveTotal: Int = 15,
    val earnedLeaveUsed: Int = 5
) {
    val totalLeave: Int get() = casualLeaveTotal + sickLeaveTotal + earnedLeaveTotal
    val totalUsed: Int get() = casualLeaveUsed + sickLeaveUsed + earnedLeaveUsed
    val totalRemaining: Int get() = totalLeave - totalUsed
}

data class LeaveRequest(
    val id: String,
    val leaveType: String, // "Casual Leave", "Sick Leave", "Earned Leave"
    val startDate: String,
    val endDate: String,
    val daysCount: Int,
    val reason: String,
    val status: String, // "Pending", "Approved", "Rejected", "Cancelled"
    val appliedDate: String,
    val approverComment: String? = null
)

data class ApprovalRequest(
    val id: String,
    val employeeName: String,
    val employeeId: String,
    val department: String,
    val requestType: String, // "Leave Request", "Attendance Correction", "Expense Claim"
    val requestDate: String,
    val details: String,
    val status: String, // "Pending", "Approved", "Rejected"
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
    val pdfUrl: String = "sample_salary_slip.pdf"
)

data class AppNotification(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val category: String, // "Attendance", "Leave", "Salary Slip", "Company Updates", "Registration Status"
    val isRead: Boolean = false,
    val targetRoute: String? = null
)
