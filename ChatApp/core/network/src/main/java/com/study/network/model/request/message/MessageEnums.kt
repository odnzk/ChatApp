package com.study.network.model.request.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MessageType {
    @SerialName("private")
    PRIVATE,

    @SerialName("stream")
    STREAM
}

@Serializable
enum class MessagesAnchor(val key: String) {
    @SerialName("newest")
    NEWEST("newest"),

    @SerialName("oldest")
    OLDEST("oldest"),

    @SerialName("first_unread")
    FIRST_UNREAD("first_unread")
}

