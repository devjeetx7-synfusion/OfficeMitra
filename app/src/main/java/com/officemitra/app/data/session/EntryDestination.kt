package com.officemitra.app.data.session

import com.officemitra.app.data.model.AccountLoadError
import com.officemitra.app.data.model.AccountState
import com.officemitra.app.data.model.MembershipState

/** The top-level screen a user is sent to when the app starts or the session changes. */
enum class EntryDestination {
    ONBOARDING,
    WELCOME,
    HOME,
    JOIN_COMPANY,
    REGISTRATION_STATUS,
    ACCOUNT_SUSPENDED,
    RESET_PASSWORD,
    NO_INTERNET,
    ERROR;

    companion object {
        /**
         * Where a signed-in user belongs, given what the server said about their account.
         *
         * The dashboard is reachable only with an approved membership. When the account could not be
         * loaded nothing is assumed: the user sees a retry screen instead of an empty dashboard or a
         * "join a company" form they may not need.
         */
        fun forAccount(state: AccountState): EntryDestination = when {
            state.loadError == AccountLoadError.NETWORK -> NO_INTERNET
            state.loadError == AccountLoadError.OTHER -> ERROR
            state.isSuspended -> ACCOUNT_SUSPENDED
            else -> when (state.membership) {
                MembershipState.APPROVED -> HOME
                MembershipState.PENDING, MembershipState.REJECTED -> REGISTRATION_STATUS
                MembershipState.NONE -> JOIN_COMPANY
                MembershipState.UNKNOWN -> ERROR
            }
        }
    }
}
