package com.study.tinkoff.feature.select_emoji.domain

import com.study.tinkoff.core.domain.model.Emoji

interface EmojiRepository {
    fun getEmoji(): List<Emoji>
    suspend fun getEmojiByEmojiName(emojiName: String): Emoji
}
