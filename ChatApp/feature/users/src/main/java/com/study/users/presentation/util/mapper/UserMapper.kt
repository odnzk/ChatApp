package com.study.users.presentation.util.mapper

import com.study.components.model.UserPresenceStatus
import com.study.users.domain.model.User
import com.study.users.presentation.model.UiUser

internal fun User.toUiUser(presence: UserPresenceStatus = UserPresenceStatus.OFFLINE): UiUser =
    UiUser(id = id, name = name, email = email, avatarUrl = avatarUrl, presence = presence)

internal fun List<User>.toOfflineUsers(): List<UiUser> = map { it.toUiUser() }
