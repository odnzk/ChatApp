package com.study.users.data.mapper


import com.study.network.model.response.user.AllUserPresenceDto
import com.study.network.model.response.user.PresenceStatusDto
import com.study.users.domain.model.UserPresence

internal fun AllUserPresenceDto.toUserPresenceList(): List<UserPresence> =
    presences?.map { (userEmail, presenceInfo) ->
        when {
            presenceInfo == null || (presenceInfo.website == null && presenceInfo.zulipMobile == null) -> {
                UserPresence(userEmail, false)
            }

            presenceInfo.website == null -> {
                UserPresence(
                    userEmail,
                    presenceInfo.zulipMobile?.status == PresenceStatusDto.ACTIVE
                )
            }

            presenceInfo.zulipMobile == null -> {
                UserPresence(userEmail, presenceInfo.website?.status == PresenceStatusDto.ACTIVE)
            }

            else -> {
                if (presenceInfo.zulipMobile!!.timestamp > presenceInfo.website!!.timestamp) {
                    UserPresence(
                        userEmail,
                        presenceInfo.zulipMobile?.status == PresenceStatusDto.ACTIVE
                    )
                } else {
                    UserPresence(
                        userEmail,
                        presenceInfo.website?.status == PresenceStatusDto.ACTIVE
                    )
                }
            }
        }
    } ?: emptyList()

