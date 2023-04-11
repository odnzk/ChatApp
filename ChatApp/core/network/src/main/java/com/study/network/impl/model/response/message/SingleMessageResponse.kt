package com.study.network.impl.model.response.message


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SingleMessageResponse(
    @SerialName("message")
    val message: MessageDto?
)
