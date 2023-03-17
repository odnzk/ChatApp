package com.study.tinkoff.core.domain.model.message

import com.study.tinkoff.core.domain.model.Client
import com.study.tinkoff.core.domain.model.Reaction

data class IncomeMessage(
    val avatarUrl: String?,
    val client: Client,
    val content: String,
    val contentType: MessageContentType,
    val displayRecipient: Any, // string | (object)[]
    val messageId: Int,
    val isMeMessage: Boolean,
    val lastEditedTimestamp: Int,
    val reactions: MutableList<Reaction>
)

data class OutcomeMessage(
    val type: MessageType,
    val to: Any, // string | integer | (string)[] | (integer)[]
    val content: String,
    val topic: String?
)

enum class MessageType {
    PRIVATE, STREAM
}

enum class MessageContentType {
    TEXT, MARKDOWN
}
