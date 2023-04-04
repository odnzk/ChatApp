package com.study.network.model.response.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PresenceDetails(
    @SerialName("status")
    val status: PresenceStatusDto?, // idle
    @SerialName("timestamp")
    val timestamp: Int? // 1680216528
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
