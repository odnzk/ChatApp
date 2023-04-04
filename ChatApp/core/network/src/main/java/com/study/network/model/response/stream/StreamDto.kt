package com.study.network.model.response.stream


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StreamDto(
    @SerialName("description") val description: String?, // A Scandinavian country
    @SerialName("invite_only") val inviteOnly: Boolean?, // false
    @SerialName("name") val name: String?, // Denmark
    @SerialName("stream_id") val streamId: Int?, // 1
    @SerialName("stream_post_policy") val streamPostPolicy: Int? // 1
)
