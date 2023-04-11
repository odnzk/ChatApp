package com.study.chat.data.mapper

import com.study.chat.domain.model.Emoji
import com.study.chat.domain.model.IncomeMessage
import com.study.chat.domain.model.Reaction
import com.study.common.extensions.unixToCalendar
import com.study.network.impl.model.response.message.AllMessagesResponse
import com.study.network.impl.model.response.message.MessageDto
import com.study.network.impl.model.response.message.ReactionDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal suspend fun AllMessagesResponse.toMessageList(): List<IncomeMessage> =
    withContext(Dispatchers.Default) {
        messages?.filterNotNull()?.map { it.toIncomeMessage() }?.sortedByDescending { it.calendar }
            ?: emptyList()
    }

internal suspend fun MessageDto.toIncomeMessage(): IncomeMessage =
    withContext(Dispatchers.Default) {
        val id = requireNotNull(id)
        IncomeMessage(
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
    messageId = messageId, userId = requireNotNull(userId), emoji = Emoji(
        name = requireNotNull(emojiName), code = requireNotNull(emojiCode)
    )
)
