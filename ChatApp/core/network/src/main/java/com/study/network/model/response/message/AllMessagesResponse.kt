package com.study.network.model.response.message


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllMessagesResponse(
    @SerialName("messages")
    val messages: List<MessageDto?>?
)
