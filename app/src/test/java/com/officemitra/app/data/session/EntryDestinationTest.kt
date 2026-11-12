package com.officemitra.app.data.session

import com.officemitra.app.data.model.AccountLoadError
import com.officemitra.app.data.model.AccountState
import com.officemitra.app.data.model.MembershipState
import org.junit.Assert.assertEquals
import org.junit.Test

class EntryDestinationTest {

    @Test
    fun `only an approved membership opens the dashboard`() {
        assertEquals(EntryDestination.HOME, EntryDestination.forAccount(AccountState(MembershipState.APPROVED)))
        assertEquals(EntryDestination.REGISTRATION_STATUS, EntryDestination.forAccount(AccountState(MembershipState.PENDING)))
        assertEquals(EntryDestination.REGISTRATION_STATUS, EntryDestination.forAccount(AccountState(MembershipState.REJECTED)))
        assertEquals(EntryDestination.JOIN_COMPANY, EntryDestination.forAccount(AccountState(MembershipState.NONE)))
    }

    @Test
    fun `an account that could not be loaded never opens the dashboard`() {
        assertEquals(EntryDestination.ERROR, EntryDestination.forAccount(AccountState()))
        assertEquals(
            EntryDestination.NO_INTERNET,
            EntryDestination.forAccount(AccountState(MembershipState.APPROVED, loadError = AccountLoadError.NETWORK))
        )
        assertEquals(
            EntryDestination.ERROR,
            EntryDestination.forAccount(AccountState(MembershipState.APPROVED, loadError = AccountLoadError.OTHER))
        )
    }

    @Test
    fun `a suspended account is blocked even with an approved membership`() {
        assertEquals(
            EntryDestination.ACCOUNT_SUSPENDED,
            EntryDestination.forAccount(AccountState(MembershipState.APPROVED, isSuspended = true))
        )
    }

    @Test
    fun `membership state is derived from every membership row`() {
        assertEquals(MembershipState.NONE, MembershipState.fromStatuses(emptyList()))
        assertEquals(MembershipState.PENDING, MembershipState.fromStatuses(listOf("pending")))
        assertEquals(MembershipState.APPROVED, MembershipState.fromStatuses(listOf("rejected", " Approved ")))
        assertEquals(MembershipState.PENDING, MembershipState.fromStatuses(listOf("rejected", "pending")))
        assertEquals(MembershipState.REJECTED, MembershipState.fromStatuses(listOf("rejected")))
        assertEquals(MembershipState.REJECTED, MembershipState.fromStatuses(listOf("revoked")))
        // An unknown status must never be treated as approved.
        assertEquals(MembershipState.REJECTED, MembershipState.fromStatuses(listOf("something-new")))
    }
}
