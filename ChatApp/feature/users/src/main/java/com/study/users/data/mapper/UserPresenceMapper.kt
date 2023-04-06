package com.study.users.data.mapper

import com.study.network.model.response.user.AllUserPresenceDto
import com.study.network.model.response.user.PresenceStatusDto
import com.study.users.domain.model.UserPresence

internal fun AllUserPresenceDto.toUserPresenceList(): List<UserPresence> {
    return presences?.map { (userEmail, presenceInfo) ->
        val isActive = presenceInfo?.website?.status == PresenceStatusDto.ACTIVE
                || presenceInfo?.zulipMobile?.status == PresenceStatusDto.ACTIVE
        UserPresence(userEmail = requireNotNull(userEmail), isActive = isActive)
    } ?: emptyList()
}
