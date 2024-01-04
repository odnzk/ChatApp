package com.study.chat.common.data.mapper

import com.study.chat.common.domain.model.IncomeMessage
import com.study.common.ext.unixToCalendar
import com.study.chat.common.data.source.local.message.entity.MessageEntity
import com.study.chat.common.data.source.local.message.entity.ReactionEntity
import com.study.chat.common.data.source.local.message.entity.MessageWithReactionsTuple
import com.study.network.model.response.message.AllMessagesResponse
import com.study.network.model.response.message.MessageDto

internal fun AllMessagesResponse.getAllReactionEntities(): List<ReactionEntity> {
    val resultList = mutableListOf<ReactionEntity>()
    messages?.filterNotNull()?.map { messageDto ->
        val id = requireNotNull(messageDto.id)
        resultList.addAll(messageDto.reactions.toReactionEntities(id))
    } ?: emptyList()
    return resultList
}

internal fun AllMessagesResponse.toMessageEntities(channelId: Int): List<MessageEntity> =
    messages?.filterNotNull()?.map { it.toMessageEntity(channelId) }
        ?.sortedByDescending { it.calendar }
        ?: emptyList()

internal fun MessageDto.toMessageEntity(channelId: Int): MessageEntity {
    val id = id
    return MessageEntity(
        id = id,
        content = content,
        calendar = timestamp.unixToCalendar(),
        senderName = senderFullName,
        senderAvatarUrl = avatarUrl,
        senderId = senderId,
        channelId = channelId,
        topicTitle = subject
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
    val id = id
    return IncomeMessage(
        id = id,
        senderAvatarUrl = avatarUrl,
        senderName = senderFullName,
        senderId = senderId,
        content = content,
        calendar = timestamp.unixToCalendar(),
        topic = subject,
        reactions = reactions.filterNotNull().map { it.toReaction(id) },
        channelId = channelId
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
