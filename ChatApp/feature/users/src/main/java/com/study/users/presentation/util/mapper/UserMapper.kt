package com.study.users.presentation.util.mapper

import com.study.components.model.UserPresenceStatus
import com.study.users.domain.model.User
import com.study.users.domain.model.UserPresence
import com.study.users.presentation.util.model.UiUser

internal fun User.toUiUser(presence: UserPresenceStatus = UserPresenceStatus.OFFLINE): UiUser =
    UiUser(id = id, name = name, email = email, avatarUrl = avatarUrl, presence = presence)

internal fun List<User>.toOfflineUsers(): List<UiUser> = map { it.toUiUser() }

internal fun List<User>.toUiUsers(presence: List<UserPresence>): List<UiUser> {
    val resList: MutableList<UiUser> = mutableListOf()
    groupBy { it.isActive }.forEach { (isActive, users) ->
        if (isActive) {
            val usersPresence = presence.sortedBy { it.userEmail }
            users.forEach { activeUser ->
                val status = usersPresence.find { it.userEmail == activeUser.email }?.let {
                    if (it.isActive) UserPresenceStatus.ACTIVE else UserPresenceStatus.IDLE
                } ?: UserPresenceStatus.OFFLINE
                resList.add(activeUser.toUiUser(status))
            }
        } else resList.addAll(users.toOfflineUsers())
    }
    return resList.sortedBy { it.presence }
}
