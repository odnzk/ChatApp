package com.study.chat.domain.repository

import com.study.chat.data.LocalEmojiRepository
import com.study.chat.domain.model.Emoji

interface EmojiRepository {
    suspend fun getEmoji(): List<Emoji>

    companion object {
        operator fun invoke(): EmojiRepository = LocalEmojiRepository
    }
}
