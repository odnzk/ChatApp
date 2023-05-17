package com.study.profile.data.mapper

import com.study.components.model.UiUserPresenceStatus
import com.study.network.model.response.user.DetailedUserDto
import com.study.network.model.response.user.PresenceStatusDto
import com.study.network.model.response.user.UserPresenceResponse
import com.study.profile.domain.model.UserDetailed

internal fun DetailedUserDto.toDetailedUser(): UserDetailed = UserDetailed(
    id = requireNotNull(userId),
    name = requireNotNull(fullName),
    email = requireNotNull(email),
    avatarUrl = avatarUrl,
    isBot = requireNotNull(isBot),
    isActive = requireNotNull(isActive)
)

internal fun UserPresenceResponse.toUserPresenceStatus(): UiUserPresenceStatus {
    val isActive = presence?.zulipMobile?.status == PresenceStatusDto.ACTIVE
            || presence?.website?.status == PresenceStatusDto.ACTIVE
    return if (isActive) UiUserPresenceStatus.ACTIVE else UiUserPresenceStatus.IDLE
}
