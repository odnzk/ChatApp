package com.study.network.model.response.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllUserPresenceDto(
    @SerialName("presences")
    val presences: Map<String?, UserPresenceDto?>?
)
