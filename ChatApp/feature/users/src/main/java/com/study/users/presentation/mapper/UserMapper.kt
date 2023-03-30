package com.study.users.presentation.mapper

import com.study.users.domain.model.User
import com.study.users.presentation.rv.UserState

private fun User.toUserState() = UserState.Success(this)
internal fun List<User>.toUserState() = map { it.toUserState() }
