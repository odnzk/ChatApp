package com.study.network.model.response.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PresenceDetails(
    @SerialName("status")
    val status: PresenceStatusDto? = PresenceStatusDto.OFFLINE,
    @SerialName("timestamp")
    val timestamp: Int
)

@Serializable
enum class PresenceStatusDto {
    @SerialName("active")
    ACTIVE,

    @SerialName("idle")
    IDLE,

    @SerialName("offline")
    OFFLINE
}
