package com.study.tinkoff.feature.emoji.domain

import com.study.domain.model.Emoji

interface EmojiRepository {
    fun getEmoji(): List<Emoji>
    suspend fun getEmojiByEmojiName(emojiName: String): Emoji
}
