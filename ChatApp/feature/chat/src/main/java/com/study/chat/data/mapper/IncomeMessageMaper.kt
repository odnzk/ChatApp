package com.study.chat.data.mapper

import com.study.chat.domain.model.Emoji
import com.study.chat.domain.model.IncomeMessage
import com.study.chat.domain.model.Reaction
import com.study.common.extensions.unixToCalendar
import com.study.network.model.response.message.AllMessagesResponse
import com.study.network.model.response.message.MessageDto
import com.study.network.model.response.message.ReactionDto

internal fun AllMessagesResponse.toMessageList(): List<IncomeMessage> =
    messages?.filterNotNull()?.map { it.toIncomeMessage() }?.sortedByDescending { it.calendar }
        ?: emptyList()


internal fun MessageDto.toIncomeMessage(): IncomeMessage {
    val id = requireNotNull(id)
    return IncomeMessage(
        id = id,
        content = requireNotNull(content),
        calendar = requireNotNull(timestamp).unixToCalendar(),
        reactions = reactions.toReactionList(id),
        senderName = requireNotNull(senderFullName),
        senderAvatarUrl = requireNotNull(avatarUrl),
        senderId = requireNotNull(senderId)
    )
}

private fun List<ReactionDto?>?.toReactionList(messageId: Int): List<Reaction> =
    this?.filterNotNull()?.map { it.toReaction(messageId) } ?: emptyList()

private fun ReactionDto.toReaction(messageId: Int): Reaction = Reaction(
    messageId = messageId,
    userId = requireNotNull(userId),
    emoji = Emoji(name = requireNotNull(emojiName), code = requireNotNull(emojiCode))
)
