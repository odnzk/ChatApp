package com.study.users.data.mapper


import com.study.network.model.response.user.AllUsersResponse
import com.study.network.model.response.user.DetailedUserDto
import com.study.users.domain.model.User

internal fun AllUsersResponse.toUserList(): List<User> =
    members?.filterNotNull()?.map { userDto -> userDto.toUser() }
        ?: emptyList()


internal fun DetailedUserDto.toUser(): User = User(
    id = userId,
    name = fullName,
    email = email,
    avatarUrl = avatarUrl,
    isBot = isBot,
    isActive = isActive
)

