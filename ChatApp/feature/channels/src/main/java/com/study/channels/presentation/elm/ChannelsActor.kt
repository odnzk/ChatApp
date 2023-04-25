package com.study.channels.presentation.elm

import android.content.Context
import com.google.android.material.color.MaterialColors
import com.study.channels.domain.exceptions.ChannelDoesNotHaveTopicsException
import com.study.channels.domain.exceptions.ChannelNotFoundException
import com.study.channels.domain.usecase.GetChannelTopicsUseCase
import com.study.channels.domain.usecase.GetChannelsUseCase
import com.study.channels.domain.usecase.SearchChannelUseCase
import com.study.channels.domain.usecase.UpdateChannelTopicsUseCase
import com.study.channels.domain.usecase.UpdateChannelsUseCase
import com.study.channels.presentation.util.mapper.toChannelsMap
import com.study.channels.presentation.util.mapper.toUiChannelTopics
import com.study.channels.presentation.util.model.UiChannel
import com.study.channels.presentation.util.model.UiChannelModel
import com.study.common.extensions.replaceFirst
import com.study.common.extensions.toFlow
import com.study.ui.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vivid.money.elmslie.core.switcher.Switcher
import vivid.money.elmslie.coroutines.Actor
import vivid.money.elmslie.coroutines.switch
import javax.inject.Inject

internal class ChannelsActor @Inject constructor(
    private val getChannelsUseCase: GetChannelsUseCase,
    private val getChannelTopicUseCase: GetChannelTopicsUseCase,
    private val searchChannelUseCase: SearchChannelUseCase,
    private val updateChannelsUseCase: UpdateChannelsUseCase,
    private val updateChannelTopicsUseCase: UpdateChannelTopicsUseCase,
    private val dispatcher: CoroutineDispatcher,
    context: Context
) : Actor<ChannelsCommand, ChannelsEvent.Internal> {

    private val channelTopicMainColor = MaterialColors.getColor(
        context,
        androidx.appcompat.R.attr.colorPrimary,
        context.getColor(R.color.navy_light)
    )
    private val switcher = Switcher()
    override fun execute(command: ChannelsCommand): Flow<ChannelsEvent.Internal> = when (command) {
        is ChannelsCommand.LoadChannels -> switcher.switch {
            getChannelsUseCase(command.filter).map { it.toChannelsMap() }.mapEvents(
                ChannelsEvent.Internal::LoadingChannelsWithTopicsSuccess,
                ChannelsEvent.Internal::LoadingError
            )
        }
        is ChannelsCommand.ManageChannelTopics -> toFlow(dispatcher) {
            manageChannelTopics(command.currentChannelsMap, command.channelId)
        }.mapEvents(
            ChannelsEvent.Internal::LoadingChannelsWithTopicsSuccess,
            ChannelsEvent.Internal::LoadingError
        )
        is ChannelsCommand.SearchChannels ->
            switcher.switch {
                searchChannelUseCase(command.query, command.filter).map { it.toChannelsMap() }
                    .mapEvents(
                        ChannelsEvent.Internal::LoadingChannelsWithTopicsSuccess,
                        ChannelsEvent.Internal::LoadingError
                    )
            }
        is ChannelsCommand.UpdateChannels ->
            toFlow { updateChannelsUseCase(command.filter) }
                .mapEvents(errorMapper = ChannelsEvent.Internal::LoadingError)
        is ChannelsCommand.UpdateChannelTopic ->
            toFlow { updateChannelTopicsUseCase(command.channelId) }
                .mapEvents(errorMapper = ChannelsEvent.Internal::LoadingError)
    }


    private suspend fun manageChannelTopics(
        channelsMap: Map<Int, List<UiChannelModel>>,
        channelId: Int
    ): Map<Int, List<UiChannelModel>> {
        val selectedChannelWithTopics = channelsMap[channelId] ?: throw ChannelNotFoundException()
        val channel = selectedChannelWithTopics.first { it is UiChannel } as UiChannel
        val updatedChannel = channel.copy(isCollapsed = !channel.isCollapsed)
        return channelsMap.toMutableMap().apply {
            val updatedChannelWIthTopics =
                if (updatedChannel.isCollapsed && selectedChannelWithTopics.size == 1) {
                    val topics = getChannelTopicUseCase(channel.id).toUiChannelTopics(
                        channel.id,
                        channel.title,
                        channelTopicMainColor
                    )
                    if (topics.isEmpty()) throw ChannelDoesNotHaveTopicsException()
                    mutableListOf<UiChannelModel>(updatedChannel).apply { addAll(topics) }
                } else {
                    selectedChannelWithTopics
                        .toMutableList()
                        .replaceFirst(updatedChannel) { it is UiChannel }
                }
            set(channelId, updatedChannelWIthTopics)
        }
    }
}
