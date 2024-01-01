package com.study.profile.presentation.util.mapper

import com.study.components.model.UiUserPresenceStatus
import com.study.profile.R
import com.study.profile.domain.model.UserDetailed
import com.study.profile.domain.model.UserPresence
import com.study.profile.presentation.model.UiUserDetailed

internal fun UserDetailed.toUiUser(presence: UiUserPresenceStatus): UiUserDetailed =
    UiUserDetailed(
        username = name,
        avatarUrl = avatarUrl,
        presence = presence,
        isBot = isBot,
        email = email,
        roles = buildList {
            if (isBot) add(R.string.user_role_bot)
            if (isOwner) add(R.string.user_role_owner)
            if (isAdmin) add(R.string.user_role_admin)
            if (isBillingAdmin) add(R.string.user_role_billing_admin)
            if (isGuest) add(R.string.user_role_guest)
        }
    )


internal fun UserPresence.toUserPresenceStatus(): UiUserPresenceStatus {
    return if (isIdle) UiUserPresenceStatus.IDLE else UiUserPresenceStatus.ACTIVE
}