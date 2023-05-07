package com.study.chat.data.mapper

import com.study.chat.domain.model.NOT_YET_SYNCHRONIZED_ID
import com.study.chat.domain.model.OutcomeMessage
import com.study.database.entity.MessageEntity

internal fun OutcomeMessage.toMessageEntity() =
    MessageEntity(
        id = NOT_YET_SYNCHRONIZED_ID,
        channelTitle = channelTitle,
        topicTitle = topicTitle,
        senderId = senderId,
        senderAvatarUrl = null,
        senderName = null,
        content = content,
        calendar = calendar
    )
