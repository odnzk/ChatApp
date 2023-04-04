package com.study.network.model.response.message


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReactionDto(
    @SerialName("emoji_code")
    val emojiCode: String?, // 1f642
    @SerialName("emoji_name")
    val emojiName: String?, // smile
    @SerialName("reaction_type")
    val reactionType: String?, // unicode_emoji
    @SerialName("user")
    val user: SenderDto?,
    @SerialName("user_id")
    val userId: Int? // 589236
)
