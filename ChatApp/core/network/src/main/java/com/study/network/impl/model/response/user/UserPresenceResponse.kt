package com.study.network.impl.model.response.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserPresenceResponse(
    @SerialName("presence")
    val presence: UserPresenceDto?
)
