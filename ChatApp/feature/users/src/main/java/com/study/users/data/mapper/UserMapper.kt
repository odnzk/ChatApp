package com.study.users.data.mapper


import com.study.network.impl.model.response.user.AllUsersResponse
import com.study.network.impl.model.response.user.DetailedUserDto
import com.study.users.domain.model.User

internal fun AllUsersResponse.toUserList(): List<User> =
    members?.filterNotNull()?.map { userDto -> userDto.toUser() } ?: emptyList()

private fun DetailedUserDto.toUser(): User = User(
    id = requireNotNull(userId),
    name = requireNotNull(fullName),
    email = requireNotNull(email),
    avatarUrl = avatarUrl.orEmpty(),
    isActive = requireNotNull(isActive)
)

