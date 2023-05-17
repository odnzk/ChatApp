package com.study.users.presentation.util.mapper

import com.study.components.model.UiUserPresenceStatus
import com.study.users.domain.model.User
import com.study.users.domain.model.UserPresence
import com.study.users.presentation.model.UiUser

private fun User.toUiUser(presence: UiUserPresenceStatus): UiUser =
    UiUser(
        id = id,
        name = name,
        email = email,
        avatarUrl = avatarUrl,
        presence = presence,
        isBot = isBot
    )

internal fun List<User>.toUiUsers(presence: List<UserPresence>): List<UiUser> =
    map { user ->
        when {
            user.isActive -> {
                val status = presence.find { it.userEmail == user.email }?.let {
                    if (it.isActive) UiUserPresenceStatus.ACTIVE else UiUserPresenceStatus.IDLE
                } ?: UiUserPresenceStatus.OFFLINE
                user.toUiUser(status)
            }
            user.isBot -> user.toUiUser(UiUserPresenceStatus.BOT)
            else -> user.toUiUser(UiUserPresenceStatus.OFFLINE)
        }
    }.sortedBy { it.presence }
