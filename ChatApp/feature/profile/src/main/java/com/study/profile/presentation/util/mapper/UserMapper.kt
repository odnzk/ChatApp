package com.study.profile.presentation.util.mapper

import com.study.components.model.UiUserPresenceStatus
import com.study.profile.domain.model.UserDetailed
import com.study.profile.domain.model.UserPresence
import com.study.profile.presentation.model.UiUserDetailed

internal fun UserDetailed.toUiUser(presence: UiUserPresenceStatus): UiUserDetailed =
    UiUserDetailed(
        username = name,
        avatarUrl = avatarUrl,
        presence = presence,
        isBot = isBot,
        email = email
    )


internal fun UserPresence.toUserPresenceStatus(): UiUserPresenceStatus {
    return if (isIdle) UiUserPresenceStatus.IDLE else UiUserPresenceStatus.ACTIVE
}