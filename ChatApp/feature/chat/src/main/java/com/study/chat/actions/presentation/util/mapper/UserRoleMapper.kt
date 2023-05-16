package com.study.chat.actions.presentation.util.mapper

import com.study.chat.actions.domain.model.UserRole
import com.study.chat.actions.presentation.model.UiUserRole

internal fun UserRole.toUiUserRole(): UiUserRole = when (this) {
    UserRole.USER -> UiUserRole.USER
    UserRole.ADMIN -> UiUserRole.ADMIN
    UserRole.OWNER -> UiUserRole.OWNER
}
