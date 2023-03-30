package com.study.chat.domain.model

data class IncomeMessage(
    val senderAvatarUrl: String?,
    val senderName: String,
    val content: String,
    val contentType: MessageContentType,
    val displayRecipient: Any,
    val messageId: Int,
    val isMeMessage: Boolean,
    val lastEditedTimestamp: Int,
    val reactions: MutableList<Reaction>
)

data class OutcomeMessage(
    val type: MessageType, val to: Any, val content: String, val topic: String?
)

enum class MessageType {
    PRIVATE, STREAM
}

enum class MessageContentType {
    TEXT, MARKDOWN
}
