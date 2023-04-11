package com.study.network.impl.model.response.stream


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StreamDto(
    @SerialName("description") val description: String?,
    @SerialName("invite_only") val inviteOnly: Boolean?,
    @SerialName("name") val name: String?,
    @SerialName("stream_id") val streamId: Int?,
    @SerialName("stream_post_policy") val streamPostPolicy: Int?
)
