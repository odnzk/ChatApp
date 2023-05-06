package com.study.network.model.request.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
class ChannelRequestDto(
    @SerialName("name")
    val title: String,
    @SerialName("description")
    val description: String
) {
    override fun toString(): String {
        val jsonChannel = Json.encodeToString(this)
        return listOf(jsonChannel).joinToString(prefix = "[", postfix = "]")
    }
}

