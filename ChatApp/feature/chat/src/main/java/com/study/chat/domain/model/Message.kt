package com.study.chat.domain.model

import com.study.network.model.request.message.MessageType
import java.util.Calendar

const val NOT_YET_SYNCHRONIZED_ID = -99

data class IncomeMessage(
    val id: Int,
    val senderAvatarUrl: String?,
    val senderName: String?,
    val senderId: Int,
    val content: String,
    val calendar: Calendar,
    val topic: String,
    val reactions: List<Reaction>
)


data class OutcomeMessage(
    val senderId: Int,
    val channelTitle: String,
    val content: String,
    val topicTitle: String,
    val calendar: Calendar,
    val type: MessageType = MessageType.STREAM
)
