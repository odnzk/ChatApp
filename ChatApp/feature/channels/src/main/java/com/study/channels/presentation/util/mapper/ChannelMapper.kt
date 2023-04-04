package com.study.channels.presentation.util.mapper

import android.graphics.Color
import com.study.channels.domain.model.Channel
import com.study.channels.domain.model.ChannelTopic
import com.study.channels.presentation.model.UiChannel
import com.study.channels.presentation.model.UiChannelModel
import com.study.channels.presentation.model.UiChannelTopic
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.TreeMap

private val mapperDefaultDispatcher = Dispatchers.Default
private suspend fun Channel.toUiChannel(
    isCollapsed: Boolean = false,
    dispatcher: CoroutineDispatcher = mapperDefaultDispatcher
): UiChannel =
    withContext(dispatcher) { UiChannel(id, title, isCollapsed) }

internal suspend fun List<Channel>.toUiChannels(
    isCollapsed: Boolean = false,
    dispatcher: CoroutineDispatcher = mapperDefaultDispatcher
) = withContext(dispatcher) { map { it.toUiChannel(isCollapsed) } }

private suspend fun ChannelTopic.toUiChannelTopic(
    channelId: Int,
    channelTitle: String,
    prevTopicLastMessageId: Int = 0,
    dispatcher: CoroutineDispatcher = mapperDefaultDispatcher
): UiChannelTopic = withContext(dispatcher) {
    UiChannelTopic(
        channelId = channelId,
        channelTitle = channelTitle,
        title = title,
        lastMessageId = lastMessageId,
        messagesCount = lastMessageId - prevTopicLastMessageId,
        backgroundColor = generateColor()
    )
}

internal suspend fun List<ChannelTopic>.toUiChannelTopics(
    channelId: Int,
    channelTitle: String,
    dispatcher: CoroutineDispatcher = mapperDefaultDispatcher
): List<UiChannelTopic> =
    withContext(dispatcher) {
        var prevLastMesId = 0
        val resList = mutableListOf<UiChannelTopic>()
        for (i in sortedBy { it.lastMessageId }) {
            resList.add(
                i.toUiChannelTopic(
                    channelId = channelId,
                    channelTitle = channelTitle,
                    prevLastMesId
                )
            )
            prevLastMesId = i.lastMessageId
        }
        resList
    }

private val colorAllowedChars = ('A'..'F') + ('a'..'f') + ('0'..'9')
private fun generateColor(): Int =
    Color.parseColor("#" + (1..6).map { colorAllowedChars.random() }.joinToString(""))


internal suspend fun List<Channel>.toChannelsTreeMap(): TreeMap<UiChannel, List<UiChannelTopic>> =
    withContext(Dispatchers.Default) {
        val map = TreeMap<UiChannel, List<UiChannelTopic>>()
        toUiChannels().forEach { channel -> map[channel] = emptyList() }
        return@withContext map
    }

internal suspend fun Map<UiChannel, List<UiChannelTopic>>.toChannelsList(): List<UiChannelModel> =
    withContext(Dispatchers.Default) {
        val resList = mutableListOf<UiChannelModel>()
        forEach { (channel, topics) ->
            resList.add(channel)
            if (channel.isCollapsed) resList.addAll(topics)
        }
        return@withContext resList
    }
