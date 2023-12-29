package com.study.profile.presentation.util.mapper

import com.study.components.model.UiUserPresenceStatus
import com.study.profile.domain.model.UserDetailed
import com.study.profile.presentation.model.UiUserDetailed

internal fun UserDetailed.toUiUser(presence: UiUserPresenceStatus): UiUserDetailed =
    UiUserDetailed(username = name, avatarUrl = avatarUrl, presence = presence, isBot = isBot, email = "")
// TODO("replace epmty string with something valid")
