package com.study.channels.presentation.elm

import com.study.channels.domain.exceptions.ChannelDoesNotHaveTopicsException
import com.study.channels.domain.exceptions.ChannelNotFoundException
import com.study.channels.domain.usecase.GetChannelTopicsUseCase
import com.study.channels.domain.usecase.GetChannelsUseCase
import com.study.channels.domain.usecase.SearchChannelUseCase
import com.study.channels.presentation.util.mapper.toChannelsMap
import com.study.channels.presentation.util.mapper.toUiChannelTopics
import com.study.channels.presentation.util.model.UiChannel
import com.study.channels.presentation.util.model.UiChannelModel
import com.study.common.extensions.replaceFirst
import com.study.common.extensions.toFlow
import kotlinx.coroutines.flow.Flow
import vivid.money.elmslie.core.switcher.Switcher
import vivid.money.elmslie.coroutines.Actor
import vivid.money.elmslie.coroutines.switch

internal class ChannelsActor(
    private val getChannelsUseCase: GetChannelsUseCase,
    private val getChannelTopicUseCase: GetChannelTopicsUseCase,
    private val searchChannelUseCase: SearchChannelUseCase
) : Actor<ChannelsCommand, ChannelsEvent.Internal> {

    private val switcher = Switcher()

    override fun execute(command: ChannelsCommand): Flow<ChannelsEvent.Internal> = when (command) {
        is ChannelsCommand.LoadChannels -> switcher.switch {
            toFlow { getChannelsUseCase(command.filter).toChannelsMap() }.mapEvents(
                ChannelsEvent.Internal::LoadingChannelsWithTopicsSuccess,
                ChannelsEvent.Internal::LoadingError
            )
        }
        is ChannelsCommand.ManageChannelTopics -> toFlow {
            manageChannelTopics(command.currentChannelsMap, command.channelId)
        }.mapEvents(
            ChannelsEvent.Internal::LoadingChannelsWithTopicsSuccess,
            ChannelsEvent.Internal::LoadingError
        )
        is ChannelsCommand.SearchChannels ->
            toFlow { searchChannelUseCase(command.query, command.filter).toChannelsMap() }
                .mapEvents(
                    ChannelsEvent.Internal::LoadingChannelsWithTopicsSuccess,
                    ChannelsEvent.Internal::LoadingError
                )
    }


    private suspend fun manageChannelTopics(
        channelsMap: Map<Int, List<UiChannelModel>>,
        channelId: Int
    ): Map<Int, List<UiChannelModel>> {
        channelsMap[channelId]?.let { uiChannelModels ->
            val channel = uiChannelModels.first { it is UiChannel } as UiChannel
            val updatedChannel = channel.copy(isCollapsed = !channel.isCollapsed)

            return channelsMap.toMutableMap().apply {
                if (updatedChannel.isCollapsed && uiChannelModels.size == 1) {
                    val topics: List<UiChannelModel> = getChannelTopicUseCase(channel.id)
                        .toUiChannelTopics(channel.id, channel.title)
                    if (topics.isEmpty()) throw ChannelDoesNotHaveTopicsException()

                    set(channelId,
                        mutableListOf<UiChannelModel>(updatedChannel).apply { addAll(topics) })
                } else {
                    set(channelId,
                        uiChannelModels.toMutableList()
                            .replaceFirst(updatedChannel) { it is UiChannel })
                }
            }
        } ?: throw ChannelNotFoundException()
    }
}
