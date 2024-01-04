package com.study.chat.edit.presentation.model

internal data class EditableMessage(
    val messageId: Int,
    val channelId: Int,
    val topic: String,
    val content: String
)
