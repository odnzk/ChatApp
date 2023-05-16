package com.study.chat.shared.data.mapper

import com.study.chat.shared.domain.model.IncomeMessage
import com.study.common.extension.unixToCalendar
import com.study.database.model.MessageEntity
import com.study.database.model.ReactionEntity
import com.study.database.model.tuple.MessageWithReactionsTuple
import com.study.network.model.response.message.AllMessagesResponse
import com.study.network.model.response.message.MessageDto

internal fun AllMessagesResponse.getAllReactionEntities(): List<ReactionEntity> {
    val resultList = mutableListOf<ReactionEntity>()
    messages?.filterNotNull()?.map { messageDto ->
        val id = requireNotNull(messageDto.id)
        resultList.addAll(messageDto.reactions?.toReactionEntities(id) ?: emptyList())
    } ?: emptyList()
    return resultList
}

internal fun AllMessagesResponse.toMessageEntities(channelId: Int): List<MessageEntity> =
    messages?.filterNotNull()?.map { it.toMessageEntity(channelId) }
        ?.sortedByDescending { it.calendar }
        ?: emptyList()

internal fun MessageDto.toMessageEntity(channelId: Int): MessageEntity {
    val id = requireNotNull(id)
    return MessageEntity(
        id = id,
        content = requireNotNull(content),
        calendar = requireNotNull(timestamp).unixToCalendar(),
        senderName = senderFullName,
        senderAvatarUrl = avatarUrl,
        senderId = requireNotNull(senderId),
        channelId = channelId,
        topicTitle = requireNotNull(subject)
    )
}


internal fun MessageWithReactionsTuple.toIncomeMessage(): IncomeMessage = IncomeMessage(
    id = message.id,
    senderAvatarUrl = message.senderAvatarUrl,
    senderName = message.senderName,
    senderId = message.senderId,
    content = message.content,
    calendar = message.calendar,
    reactions = reactions.toReactions(),
    topic = message.topicTitle,
    channelId = message.channelId
)

internal fun AllMessagesResponse.toFirstMessageSenderId(messageId: Int): IncomeMessage? =
    messages?.filterNotNull()?.find { it.id == messageId }?.toIncomeMessage()

internal fun MessageDto.toIncomeMessage(): IncomeMessage {
    val id = requireNotNull(id)
    return IncomeMessage(
        id = id,
        senderAvatarUrl = avatarUrl,
        senderName = senderFullName,
        senderId = requireNotNull(senderId),
        content = requireNotNull(content),
        calendar = requireNotNull(timestamp).unixToCalendar(),
        topic = requireNotNull(subject),
        reactions = reactions?.filterNotNull()?.map { it.toReaction(id) } ?: emptyList(),
        channelId = requireNotNull(channelId)
    )
}

internal fun MessageEntity.toIncomeMessage(): IncomeMessage = IncomeMessage(
    id = id,
    senderAvatarUrl = senderAvatarUrl,
    senderName = senderName,
    senderId = senderId,
    content = content,
    calendar = calendar,
    topic = topicTitle,
    reactions = emptyList(),
    channelId = channelId
)
