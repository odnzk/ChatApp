package com.study.profile.presentation.model

import com.study.components.model.UiUserPresenceStatus

internal class UiUserDetailed(
    val username: String,
    val avatarUrl: String?,
    val email: String,
    val isBot: Boolean,
    val presence: UiUserPresenceStatus
)
