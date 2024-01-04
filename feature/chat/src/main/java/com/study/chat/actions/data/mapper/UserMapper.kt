package com.study.chat.actions.data.mapper

import com.study.chat.actions.domain.model.User
import com.study.network.model.response.user.DetailedUserDto

internal fun DetailedUserDto.toUser(): User = User(userId = userId, isAdmin = isAdmin)