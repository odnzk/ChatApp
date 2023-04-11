package com.study.network.impl.model.response.message


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    @SerialName("avatar_url") val avatarUrl: String?,
    @SerialName("client") val client: String?,
    @SerialName("content") val content: String?,
    @SerialName("content_type") val contentType: String?,
    @SerialName("flags") val flags: List<String?>?,
    @SerialName("id") val id: Int?,
    @SerialName("is_me_message") val isMeMessage: Boolean?,
    @SerialName("reactions") val reactions: List<ReactionDto?>?,
    @SerialName("recipient_id") val recipientId: Int?,
    @SerialName("sender_email") val senderEmail: String?,
    @SerialName("sender_full_name") val senderFullName: String?,
    @SerialName("sender_id") val senderId: Int?,
    @SerialName("sender_realm_str") val senderRealmStr: String?,
    @SerialName("subject") val subject: String?,
    @SerialName("timestamp") val timestamp: Int?,
    @SerialName("type") val type: com.study.network.impl.model.request.message.MessageType?
)
