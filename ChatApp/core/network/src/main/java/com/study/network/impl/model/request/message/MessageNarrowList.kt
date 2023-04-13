package com.study.network.impl.model.request.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
class MessageNarrowList(
    private vararg val narrows: MessageNarrow
) {
    override fun toString(): String {
        return narrows.contentToString()
    }
}

@Serializable
class MessageNarrow(
    @SerialName("operator") val operator: MessageNarrowOperator,
    @SerialName("operand") val operand: String
) {
    override fun toString(): String {
        return Json.encodeToString(value = this)
    }
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

