package com.officemitra.app

import com.officemitra.app.data.repository.DemoRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DemoRepositoryTest {

    private lateinit var repository: DemoRepository

    @Before
    fun setUp() {
        repository = DemoRepository()
    }

    @Test
    fun testPunchInAndPunchOutToggle() {
        assertFalse(repository.isPunchedIn.value)

        // Toggle Punch In
        repository.togglePunchInStatus()
        assertTrue(repository.isPunchedIn.value)
        assertEquals("Present", repository.todayAttendance.value.status)
        assertNotEquals("--:--", repository.todayAttendance.value.punchIn)

        // Toggle Punch Out
        repository.togglePunchInStatus()
        assertFalse(repository.isPunchedIn.value)
        assertNotEquals("--:--", repository.todayAttendance.value.punchOut)
    }

    @Test
    fun testApplyLeaveAddsToPendingRequests() {
        val initialCount = repository.leaveRequests.value.size
        repository.applyLeave("Casual Leave", "20 May 2024", "21 May 2024", 2, "Personal Work")

        val updatedRequests = repository.leaveRequests.value
        assertEquals(initialCount + 1, updatedRequests.size)
        assertEquals("Casual Leave", updatedRequests.first().leaveType)
        assertEquals("Pending", updatedRequests.first().status)
    }

    @Test
    fun testUpdateApprovalStatus() {
        val targetRequest = repository.approvalRequests.value.first()
        repository.updateApprovalStatus(targetRequest.id, true, "Approved by Manager")

        val updated = repository.approvalRequests.value.find { it.id == targetRequest.id }
        assertNotNull(updated)
        assertEquals("Approved", updated?.status)
        assertEquals("Approved by Manager", updated?.decisionComment)
    }

    @Test
    fun testUpdateProfileInfo() {
        repository.updateProfile("Alexander Morgan", "alex.m@officemitra.com", "+1 555-999-0000")

        val user = repository.currentUser.value
        assertEquals("Alexander Morgan", user.name)
        assertEquals("alex.m@officemitra.com", user.email)
        assertEquals("+1 555-999-0000", user.phone)
    }

    @Test
    fun testMarkNotificationsRead() {
        val noti = repository.notifications.value.first()
        repository.markNotificationAsRead(noti.id)

        val updated = repository.notifications.value.find { it.id == noti.id }
        assertTrue(updated?.isRead == true)
    }
}
