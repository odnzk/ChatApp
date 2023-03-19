package com.study.tinkoff.feature.channels.presentation.mapper

import android.graphics.Color
import com.study.domain.model.Channel
import com.study.domain.model.ChannelTopic
import com.study.tinkoff.feature.channels.presentation.model.UiChannel
import com.study.tinkoff.feature.channels.presentation.model.UiChannelTopic

private fun Channel.toUiChannel(isCollapsed: Boolean = false): UiChannel =
    UiChannel(id, title, isCollapsed)

fun List<Channel>.toUiChannels(isCollapsed: Boolean = false) = map { it.toUiChannel(isCollapsed) }

private fun ChannelTopic.toUiChannelTopic(
    channelId: Int, prevTopicLastMessageId: Int = 0
): UiChannelTopic = UiChannelTopic(
    channelId = channelId,
    title = title,
    lastMessageId = lastMessageId,
    messagesCount = lastMessageId - prevTopicLastMessageId,
    backgroundColor = generateColor()
)

fun List<ChannelTopic>.toUiChannelTopics(channelId: Int): List<UiChannelTopic> {
    var prevLastMesId = 0
    val resList = mutableListOf<UiChannelTopic>()
    for (i in sortedBy { it.lastMessageId }) {
        resList.add(i.toUiChannelTopic(channelId = channelId, prevLastMesId))
        prevLastMesId = i.lastMessageId
    }
    return resList
}

private val allowedChars = ('A'..'F') + ('a'..'f') + ('0'..'9')
private fun generateColor(): Int {
    return Color.parseColor("#" + (1..6).map { allowedChars.random() }.joinToString(""))
}
