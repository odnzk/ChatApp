package com.study.profile.data.mapper

import com.study.components.model.UserPresenceStatus
import com.study.network.impl.model.response.user.DetailedUserDto
import com.study.network.impl.model.response.user.PresenceStatusDto
import com.study.network.impl.model.response.user.UserPresenceResponse

import com.study.profile.domain.model.UserDetailed

internal fun DetailedUserDto.toDetailedUser(): UserDetailed = UserDetailed(
    id = requireNotNull(userId),
    name = requireNotNull(fullName),
    email = requireNotNull(email),
    avatarUrl = requireNotNull(avatarUrl),
    isActive = requireNotNull(isActive)
)

internal fun UserPresenceResponse.toUserPresenceStatus(): UserPresenceStatus {
    val isActive = presence?.zulipMobile?.status == PresenceStatusDto.ACTIVE
            || presence?.website?.status == PresenceStatusDto.ACTIVE
    return if (isActive) UserPresenceStatus.ACTIVE else UserPresenceStatus.IDLE
}
