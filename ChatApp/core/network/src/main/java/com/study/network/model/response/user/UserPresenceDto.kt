package com.study.network.model.response.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserPresenceDto(
    @SerialName("ZulipMobile")
    val zulipMobile: PresenceDetails? = null,
    @SerialName("website")
    val website: PresenceDetails? = null
)
