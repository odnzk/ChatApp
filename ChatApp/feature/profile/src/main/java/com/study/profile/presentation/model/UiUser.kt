package com.study.profile.presentation.model

import com.study.components.model.UserPresenceStatus

internal class UiUser(
    val username: String,
    val avatarUrl: String?,
    val isBot: Boolean,
    val presence: UserPresenceStatus
)
