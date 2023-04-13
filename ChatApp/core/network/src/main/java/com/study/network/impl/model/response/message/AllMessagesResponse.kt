package com.study.network.impl.model.response.message


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllMessagesResponse(
    @SerialName("messages")
    val messages: List<MessageDto?>?
)
