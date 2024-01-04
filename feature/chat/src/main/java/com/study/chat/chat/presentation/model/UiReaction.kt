package com.study.chat.chat.presentation.model

internal data class UiReaction(
    val messageId: Int,
    val emojiUnicode: String,
    val emoji: UiEmoji,
    val count: Int = 0,
    val isSelected: Boolean = false
)
