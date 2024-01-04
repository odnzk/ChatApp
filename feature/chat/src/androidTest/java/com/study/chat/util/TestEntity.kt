package com.study.chat.util

import com.study.chat.common.data.source.local.message.entity.MessageEntity
import java.util.Calendar


const val TEST_CHANNEL = -1
const val TEST_TOPIC = "test"

fun createMessageEntity(
    channelId: Int = TEST_CHANNEL,
    topic: String = TEST_TOPIC,
    id: Int = 0
): MessageEntity =
    MessageEntity(
        id = id,
        topicTitle = topic,
        senderAvatarUrl = null,
        content = channelId.toString(),
        calendar = Calendar.getInstance(),
        senderId = channelId,
        senderName = channelId.toString(),
        channelId = channelId
    )
