package com.study.chat.util

import com.study.database.model.MessageEntity
import java.util.Calendar


const val TEST_CHANNEL = "general"
const val TEST_TOPIC = "test"

fun createMessageEntity(
    channel: String = TEST_CHANNEL,
    topic: String = TEST_TOPIC,
    id: Int = 0
): MessageEntity =
    MessageEntity(
        id = id,
        channelTitle = channel,
        topicTitle = topic,
        senderAvatarUrl = null,
        content = channel,
        calendar = Calendar.getInstance(),
        senderId = channel.length,
        senderName = channel
    )
