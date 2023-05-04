package com.study.chat.presentation.chat.util.model

import com.study.chat.domain.model.Emoji

internal data class UiReaction(
    val messageId: Int,
    val emojiUnicode: String,
    val emoji: Emoji,
    val count: Int = 0,
    val isSelected: Boolean = false
)
