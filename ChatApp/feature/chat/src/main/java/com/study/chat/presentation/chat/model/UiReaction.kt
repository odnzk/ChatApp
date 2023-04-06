package com.study.chat.presentation.chat.model

import com.study.chat.domain.model.Emoji

internal data class UiReaction(
    val messageId: Int,
    val emoji: Emoji,
    val count: Int = 0,
    val isSelected: Boolean = false
)
