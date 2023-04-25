package com.study.network.model.response.stream


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StreamDto(
    @SerialName("name") val name: String?,
    @SerialName("stream_id") val streamId: Int?,
)
