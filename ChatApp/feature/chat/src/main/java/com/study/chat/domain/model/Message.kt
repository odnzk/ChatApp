package com.study.chat.domain.model

import com.study.network.model.request.message.MessageType
import java.util.Calendar

internal const val NOT_YET_SYNCHRONIZED_ID = -1

data class IncomeMessage(
    val id: Int,
    val channelId: Int,
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
    val channelId: Int,
    val content: String,
    val topicTitle: String,
    val calendar: Calendar,
    val type: MessageType = MessageType.STREAM
) {
    companion object {
        fun defaultOutcomeMessage(content: String, topic: String): OutcomeMessage =
            OutcomeMessage(
                senderId = 0,
                channelId = 0,
                content = content,
                topicTitle = topic,
                calendar = Calendar.getInstance()
            )
    }
}
