package com.study.chat.presentation.chat.util.model

import com.study.chat.presentation.util.model.UiEmoji

internal data class UiReaction(
    val messageId: Int,
    val emojiUnicode: String,
    val emoji: UiEmoji,
    val count: Int = 0,
    val isSelected: Boolean = false
)
