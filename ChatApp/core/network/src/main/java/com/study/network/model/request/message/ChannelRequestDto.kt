package com.study.network.model.request.message

import com.study.network.util.toJsonArrayString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
class ChannelRequestDto(
    @SerialName("name")
    val title: String
) {
    override fun toString(): String = Json.encodeToString(this).toJsonArrayString()
}

