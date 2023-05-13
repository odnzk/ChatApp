package com.study.channels.presentation.channels.util.mapper

import android.graphics.Color
import com.study.channels.domain.model.Channel
import com.study.channels.domain.model.ChannelTopic
import com.study.channels.presentation.channels.util.model.UiChannel
import com.study.channels.presentation.channels.util.model.UiChannelModel
import com.study.channels.presentation.channels.util.model.UiChannelTopic

private fun Channel.toUiChannel(isCollapsed: Boolean = false): UiChannel =
    UiChannel(
        id,
        title = title,
        isCollapsed = isCollapsed,
        color = color?.let { Color.parseColor(color) }
    )

internal fun List<ChannelTopic>.toUiChannelTopics(
    channelId: Int,
    channelTitle: String,
    color: Int?
): List<UiChannelTopic> =
    sortedBy { it.title }.map {
        UiChannelTopic(
            channelId = channelId,
            channelTitle = channelTitle,
            title = it.title,
            messagesCount = it.messagesCount,
            backgroundColor = color
        )
    }

internal fun List<Channel>.toChannelsMap(): Map<Int, List<UiChannelModel>> =
    associate { channel -> channel.id to listOf(channel.toUiChannel()) }

internal fun Map<Int, List<UiChannelModel>>.toChannelsList(): List<UiChannelModel> {
    val resList = mutableListOf<UiChannelModel>()
    values.forEach { channelWithTopics ->
        val channel = channelWithTopics.first { it is UiChannel } as UiChannel
        if (channel.isCollapsed) {
            resList.addAll(channelWithTopics)
        } else {
            resList.add(channel)
        }
    }
    return resList
}
