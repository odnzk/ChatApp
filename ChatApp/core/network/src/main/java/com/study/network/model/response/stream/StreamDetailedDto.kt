package com.study.network.model.response.stream


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StreamDetailedDto(
    @SerialName("can_remove_subscribers_group_id")
    val canRemoveSubscribersGroupId: Int?, // 2
    @SerialName("description")
    val description: String?, // A Scandinavian country
    @SerialName("first_message_id")
    val firstMessageId: Int?, // 1
    @SerialName("history_public_to_subscribers")
    val historyPublicToSubscribers: Boolean?, // true
    @SerialName("invite_only")
    val inviteOnly: Boolean?, // false
    @SerialName("is_announcement_only")
    val isAnnouncementOnly: Boolean?, // false
    @SerialName("is_web_public")
    val isWebPublic: Boolean?, // false
    @SerialName("message_retention_days")
    val messageRetentionDays: Int?, // null
    @SerialName("name")
    val name: String?, // Denmark
    @SerialName("rendered_description")
    val renderedDescription: String?, // <p>A Scandinavian country</p>
    @SerialName("stream_id")
    val streamId: Int?, // 7
    @SerialName("stream_post_policy")
    val streamPostPolicy: Int? // 1
)
