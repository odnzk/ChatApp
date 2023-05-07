package com.study.chat.data.mapper

import com.study.chat.domain.model.Emoji
import com.study.chat.domain.model.IncomeMessage
import com.study.chat.domain.model.Reaction
import com.study.common.extension.unixToCalendar
import com.study.database.entity.MessageEntity
import com.study.database.entity.ReactionEntity
import com.study.database.entity.tuple.MessageWithReactionsTuple
import com.study.network.model.response.message.AllMessagesResponse
import com.study.network.model.response.message.MessageDto
import com.study.network.model.response.message.ReactionDto

internal fun AllMessagesResponse.getAllReactionEntities(): List<ReactionEntity> {
    val resultList = mutableListOf<ReactionEntity>()
    messages?.filterNotNull()?.map { messageDto ->
        val id = requireNotNull(messageDto.id)
        resultList.addAll(messageDto.reactions?.toReactionEntities(id) ?: emptyList())
    } ?: emptyList()
    return resultList
}

internal fun AllMessagesResponse.toMessageEntities(
    channelTitle: String
): List<MessageEntity> =
    messages?.filterNotNull()?.map { it.toMessageEntity(channelTitle) }
        ?.sortedByDescending { it.calendar }
        ?: emptyList()

internal fun MessageDto.toMessageEntity(channelTitle: String): MessageEntity {
    val id = requireNotNull(id)
    return MessageEntity(
        id = id,
        content = requireNotNull(content),
        calendar = requireNotNull(timestamp).unixToCalendar(),
        senderName = senderFullName,
        senderAvatarUrl = avatarUrl,
        senderId = requireNotNull(senderId),
        channelTitle = channelTitle,
        topicTitle = requireNotNull(subject)
    )
}

private fun List<ReactionDto?>?.toReactionEntities(messageId: Int): List<ReactionEntity> =
    this?.filterNotNull()?.map { it.toReactionEntity(messageId) } ?: emptyList()

private fun ReactionDto.toReactionEntity(messageId: Int): ReactionEntity = ReactionEntity(
    messageId = messageId,
    userId = requireNotNull(userId),
    emojiName = requireNotNull(emojiName),
    emojiCode = requireNotNull(emojiCode)
)

private fun ReactionEntity.toReaction(): Reaction =
    Reaction(messageId = messageId, userId = userId, emoji = Emoji(emojiName, emojiCode))


internal fun List<ReactionEntity>.toReactions() = map { it.toReaction() }
internal fun MessageWithReactionsTuple.toIncomeMessage(): IncomeMessage = IncomeMessage(
    id = message.id,
    senderAvatarUrl = message.senderAvatarUrl,
    senderName = message.senderName,
    senderId = message.senderId,
    content = message.content,
    calendar = message.calendar,
    reactions = reactions.toReactions(),
    topic = message.topicTitle
)
