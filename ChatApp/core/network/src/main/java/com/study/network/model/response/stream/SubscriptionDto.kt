package com.study.network.model.response.stream


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionDto(
    @SerialName("audible_notifications")
    val audibleNotifications: Boolean?, // true
    @SerialName("color")
    val color: String?, // #e79ab5
    @SerialName("description")
    val description: String?, // A Scandinavian country
    @SerialName("desktop_notifications")
    val desktopNotifications: Boolean?, // true
    @SerialName("email_address")
    val emailAddress: String?, // Denmark+187b4125ed36d6af8b5d03ef4f65c0cf@zulipdev.com:9981
    @SerialName("invite_only")
    val inviteOnly: Boolean?, // false
    @SerialName("is_muted")
    val isMuted: Boolean?, // false
    @SerialName("name")
    val name: String?, // Denmark
    @SerialName("pin_to_top")
    val pinToTop: Boolean?, // false
    @SerialName("push_notifications")
    val pushNotifications: Boolean?, // false
    @SerialName("stream_id")
    val streamId: Int?, // 1
    @SerialName("subscribers")
    val subscribers: List<Int?>?,
    @SerialName("stream_post_policy") val streamPostPolicy: Int? // 1
)
