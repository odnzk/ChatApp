package com.study.network.model.request.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MessageType {
    @SerialName("stream")
    STREAM
}

@Serializable
enum class MessagesAnchor {
    @SerialName("newest")
    NEWEST
}

@Serializable
enum class MessageNarrowOperator {
    @SerialName("stream")
    STREAM,

    @SerialName("topic")
    TOPIC,

    @SerialName("search")
    SEARCH

}
