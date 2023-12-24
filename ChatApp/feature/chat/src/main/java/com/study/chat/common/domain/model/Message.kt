package com.study.chat.common.domain.model

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

sealed class OutcomeMessage(
    open val channelId: Int,
    open val topicTitle: String,
    open val senderId: Int,
    open val calendar: Calendar = Calendar.getInstance(),
    open val type: MessageType = MessageType.STREAM
) {

    class Text(
        override val channelId: Int,
        override val topicTitle: String,
        override val senderId: Int,
        val content: String
    ) : OutcomeMessage(
        channelId = channelId,
        topicTitle = topicTitle,
        senderId = senderId
    )

    class File(
        override val channelId: Int,
        override val topicTitle: String,
        override val senderId: Int,
        val uri: String
    ) : OutcomeMessage(
        channelId = channelId,
        topicTitle = topicTitle,
        senderId = senderId
    )
}
