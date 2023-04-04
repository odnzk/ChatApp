package com.study.chat.domain.model

import com.study.network.model.request.message.MessageType
import java.util.Calendar

data class IncomeMessage(
    val id: Int,
    val senderAvatarUrl: String?,
    val senderName: String,
    val senderId: Int,
    val content: String,
    val calendar: Calendar,
    val reactions: List<Reaction>
)


data class OutcomeMessage(
    val channelTitle: String,
    val content: String,
    val topicTitle: String,
    val type: MessageType = MessageType.STREAM
)
