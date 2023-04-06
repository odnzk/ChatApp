package com.study.network.model.response.message


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SentMessageResponse(
    @SerialName("id")
    val id: Int? // 42
)
