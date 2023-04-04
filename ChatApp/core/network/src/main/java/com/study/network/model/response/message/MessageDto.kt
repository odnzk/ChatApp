package com.study.network.model.response.message


import com.study.network.model.request.message.MessageType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    @SerialName("avatar_url") val avatarUrl: String?, // https://secure.gravatar.com/avatar/6d8cad0fd00256e7b40691d27ddfd466?d=identicon&version=1
    @SerialName("client") val client: String?, // populate_db
    @SerialName("content") val content: String?, // <p>Security experts agree that relational algorithms are an interesting new topic in the field of networking, and scholars concur.</p>
    @SerialName("content_type") val contentType: String?, // text/html
    @SerialName("flags") val flags: List<String?>?,
    @SerialName("id") val id: Int?, // 16
    @SerialName("is_me_message") val isMeMessage: Boolean?, // false
    @SerialName("reactions") val reactions: List<ReactionDto?>?,
    @SerialName("recipient_id") val recipientId: Int?, // 27
    @SerialName("sender_email") val senderEmail: String?, // hamlet@zulip.com
    @SerialName("sender_full_name") val senderFullName: String?, // King Hamlet
    @SerialName("sender_id") val senderId: Int?, // 4
    @SerialName("sender_realm_str") val senderRealmStr: String?, // zulip
    @SerialName("subject") val subject: String?,
    @SerialName("timestamp") val timestamp: Int?, // 1527921326
    @SerialName("type") val type: MessageType? // private
)
