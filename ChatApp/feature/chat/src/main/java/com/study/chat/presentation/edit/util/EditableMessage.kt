package com.study.chat.presentation.edit.util

internal data class EditableMessage(
    val messageId: Int,
    val channelId: Int,
    val topic: String,
    val content: String
)
