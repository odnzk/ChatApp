package com.study.network.model.response.message


import com.study.network.model.request.message.MessageType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    @SerialName("avatar_url") val avatarUrl: String?,
    @SerialName("content") val content: String,
    @SerialName("id") val id: Int,
    @SerialName("is_me_message") val isMeMessage: Boolean,
    @SerialName("reactions") val reactions: List<ReactionDto?>,
    @SerialName("sender_full_name") val senderFullName: String,
    @SerialName("sender_id") val senderId: Int,
    @SerialName("subject") val subject: String,
    @SerialName("timestamp") val timestamp: Int,
    @SerialName("type") val type: MessageType,
    @SerialName("stream_id") val channelId: Int
)
