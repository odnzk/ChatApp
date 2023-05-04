package com.study.profile.presentation.util.mapper

import com.study.components.model.UserPresenceStatus
import com.study.profile.domain.model.UserDetailed
import com.study.profile.presentation.util.model.UiUser

internal fun UserDetailed.toUiUser(presence: UserPresenceStatus): UiUser =
    UiUser(username = name, avatarUrl = avatarUrl, presence = presence, isBot = isBot)
