package com.study.profile.data.mapper

import com.study.network.model.response.user.DetailedUserDto
import com.study.network.model.response.user.PresenceStatusDto
import com.study.network.model.response.user.UserPresenceResponse
import com.study.profile.domain.model.UserDetailed
import com.study.profile.domain.model.UserPresence

internal fun DetailedUserDto.toDetailedUser(): UserDetailed = UserDetailed(
    id = userId,
    name = fullName,
    email = email,
    avatarUrl = avatarUrl,
    isBot = isBot,
    isActive = isActive,
    isAdmin = isAdmin,
    isBillingAdmin = isBillingAdmin,
    isGuest = isGuest,
    isOwner = isOwner
)

internal fun UserPresenceResponse.toUserPresenceStatus(): UserPresence = UserPresence(
    isIdle = when {
        presence == null || (presence!!.zulipMobile == null && presence!!.zulipMobile == null) -> true
        presence!!.zulipMobile?.status == null -> presence!!.website?.status != PresenceStatusDto.ACTIVE
        presence!!.website?.status == null -> presence!!.zulipMobile?.status != PresenceStatusDto.ACTIVE
        else -> {
            if (presence!!.zulipMobile!!.timestamp > presence!!.website!!.timestamp) {
                presence!!.zulipMobile?.status != PresenceStatusDto.ACTIVE
            } else {
                presence!!.website?.status != PresenceStatusDto.ACTIVE
            }
        }
    }
)
