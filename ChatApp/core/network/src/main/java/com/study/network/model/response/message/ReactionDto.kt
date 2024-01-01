package com.study.network.model.response.message


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReactionDto(
    @SerialName("emoji_code")
    val emojiCode: String,
    @SerialName("emoji_name")
    val emojiName: String,
    @SerialName("user_id")
    val userId: Int
)
