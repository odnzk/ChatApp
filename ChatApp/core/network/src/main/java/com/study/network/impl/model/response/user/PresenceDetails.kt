package com.study.network.impl.model.response.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PresenceDetails(
    @SerialName("status")
    val status: PresenceStatusDto?,
    @SerialName("timestamp")
    val timestamp: Int?
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
