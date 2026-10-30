package com.officemitra.app

import com.officemitra.app.data.model.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UserAndDataUnitTest {

    @Test
    fun testDefaultUserFieldsAreNotFakeProductionData() {
        val defaultUser = User()

        assertEquals("", defaultUser.name)
        assertEquals("", defaultUser.companyName)
        assertEquals("", defaultUser.employeeId)
        assertEquals("", defaultUser.department)
        assertEquals("", defaultUser.designation)
        assertFalse(defaultUser.isAuthorizedApprover)
        assertEquals(ApprovalStatus.PENDING, defaultUser.approvalStatus)
    }

    @Test
    fun testLeaveBalanceCalculationsWithZeroUsed() {
        val balance = LeaveBalance(
            casualLeaveTotal = 12,
            casualLeaveUsed = 0,
            sickLeaveTotal = 10,
            sickLeaveUsed = 0,
            earnedLeaveTotal = 15,
            earnedLeaveUsed = 0
        )

        assertEquals(37, balance.totalLeave)
        assertEquals(0, balance.totalUsed)
        assertEquals(37, balance.totalRemaining)
    }

    @Test
    fun testLeaveBalanceRemainingNonNegative() {
        val balance = LeaveBalance(
            casualLeaveTotal = 5,
            casualLeaveUsed = 10,
            sickLeaveTotal = 0,
            sickLeaveUsed = 0,
            earnedLeaveTotal = 0,
            earnedLeaveUsed = 0
        )

        assertEquals(0, balance.totalRemaining)
    }

    @Test
    fun testAttendanceRecordDefaultValues() {
        val record = AttendanceRecord()

        assertEquals("Not Punched", record.status)
        assertEquals("--:--", record.punchIn)
        assertEquals("--:--", record.punchOut)
        assertEquals("0h 0m", record.workingDuration)
    }

    @Test
    fun testSalarySlipPdfUrlDefault() {
        val slip = SalarySlip(
            id = "SLIP-1",
            monthYear = "May 2025",
            period = "01 May - 31 May",
            publishDate = "01 Jun 2025",
            basicSalary = "$5,000",
            netSalary = "$4,200"
        )

        assertEquals("", slip.pdfUrl)
    }
}
