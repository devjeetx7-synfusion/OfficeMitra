package com.officemitra.app.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ModelUnitTest {

    @Test
    fun testLeaveBalanceCalculations() {
        val balance = LeaveBalance(
            casualLeaveTotal = 12,
            casualLeaveUsed = 3,
            sickLeaveTotal = 10,
            sickLeaveUsed = 2,
            earnedLeaveTotal = 15,
            earnedLeaveUsed = 5
        )

        assertEquals(37, balance.totalLeave)
        assertEquals(10, balance.totalUsed)
        assertEquals(27, balance.totalRemaining)
    }

    @Test
    fun testUserApprovalStatusEnum() {
        val approvedStatus = ApprovalStatus.APPROVED
        val pendingStatus = ApprovalStatus.PENDING
        val rejectedStatus = ApprovalStatus.REJECTED

        assertEquals("APPROVED", approvedStatus.name)
        assertEquals("PENDING", pendingStatus.name)
        assertEquals("REJECTED", rejectedStatus.name)
    }

    @Test
    fun testAppNotificationDefaultValues() {
        val noti = AppNotification(
            id = "NOTI-1",
            title = "Title",
            message = "Message",
            timestamp = "10:00 AM",
            category = "Leave"
        )

        assertFalse(noti.isRead)
    }
}
