package com.study.network.model.response.stream


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionDto(
    @SerialName("audible_notifications")
    val audibleNotifications: Boolean?,
    @SerialName("color")
    val color: String?,
    @SerialName("description")
    val description: String?,
    @SerialName("desktop_notifications")
    val desktopNotifications: Boolean?,
    @SerialName("email_address")
    val emailAddress: String?,
    @SerialName("invite_only")
    val inviteOnly: Boolean?,
    @SerialName("is_muted")
    val isMuted: Boolean?,
    @SerialName("name")
    val name: String?,
    @SerialName("pin_to_top")
    val pinToTop: Boolean?,
    @SerialName("push_notifications")
    val pushNotifications: Boolean?,
    @SerialName("stream_id")
    val streamId: Int?,
    @SerialName("subscribers")
    val subscribers: List<Int?>?,
    @SerialName("stream_post_policy") val streamPostPolicy: Int?
)
