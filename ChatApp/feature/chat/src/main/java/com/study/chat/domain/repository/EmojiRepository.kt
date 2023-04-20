package com.study.chat.domain.repository

import com.study.chat.domain.model.Emoji

interface EmojiRepository {
    suspend fun getEmoji(): List<Emoji>
}
