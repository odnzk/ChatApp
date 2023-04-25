package com.study.network.model.response.stream


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StreamDetailedDto(
    @SerialName("description")
    val description: String?,
    @SerialName("first_message_id")
    val firstMessageId: Int?,
    @SerialName("name")
    val name: String?,
    @SerialName("stream_id")
    val streamId: Int?
)
