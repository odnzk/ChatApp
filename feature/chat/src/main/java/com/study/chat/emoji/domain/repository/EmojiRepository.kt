package com.study.chat.emoji.domain.repository

import com.study.chat.common.domain.model.Emoji

internal interface EmojiRepository {
    suspend fun getEmoji(): List<Emoji>
}
