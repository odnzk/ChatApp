package com.study.chat.presentation.actions.util.mapper

import com.study.chat.domain.model.UserRole
import com.study.chat.presentation.actions.util.model.UiUserRole

internal fun UserRole.toUiUserRole(): UiUserRole = when (this) {
    UserRole.USER -> UiUserRole.USER
    UserRole.ADMIN -> UiUserRole.ADMIN
    UserRole.OWNER -> UiUserRole.OWNER
}
