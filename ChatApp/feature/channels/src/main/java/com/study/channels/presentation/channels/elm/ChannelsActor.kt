package com.study.channels.presentation.channels.elm

import com.study.channels.domain.exceptions.ChannelNotFoundException
import com.study.channels.domain.usecase.GetChannelTopicsUseCase
import com.study.channels.domain.usecase.GetChannelsUseCase
import com.study.channels.domain.usecase.LoadChannelTopicsUseCase
import com.study.channels.domain.usecase.SearchChannelUseCase
import com.study.channels.domain.usecase.UpdateChannelsUseCase
import com.study.channels.presentation.channels.util.mapper.toChannelsMap
import com.study.channels.presentation.channels.util.mapper.toUiChannelTopics
import com.study.channels.presentation.channels.util.model.UiChannel
import com.study.channels.presentation.channels.util.model.UiChannelFilter
import com.study.channels.presentation.channels.util.model.UiChannelModel
import com.study.channels.presentation.channels.util.model.UiChannelTopic
import com.study.common.extension.firstInstance
import com.study.common.extension.toFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vivid.money.elmslie.core.switcher.Switcher
import vivid.money.elmslie.coroutines.Actor
import vivid.money.elmslie.coroutines.switch
import javax.inject.Inject

internal class ChannelsActor @Inject constructor(
    private val getChannelsUseCase: GetChannelsUseCase,
    private val getChannelTopicsUseCase: GetChannelTopicsUseCase,
    private val searchChannelUseCase: SearchChannelUseCase,
    private val updateChannelsUseCase: UpdateChannelsUseCase,
    private val loadChannelTopicsUseCase: LoadChannelTopicsUseCase
) : Actor<ChannelsCommand, ChannelsEvent.Internal> {

    private val channelSwitcher = Switcher()
    private val topicSwitcher = Switcher()

    override fun execute(command: ChannelsCommand): Flow<ChannelsEvent.Internal> = when (command) {
        is ChannelsCommand.GetChannels -> channelSwitcher.switch {
            getChannelsUseCase(command.filter.isSubscribed()).map { it.toChannelsMap() }.mapEvents(
                ChannelsEvent.Internal::LoadingChannelsWithTopicsSuccess,
                ChannelsEvent.Internal::LoadingError
            )
        }
        is ChannelsCommand.ShowChannelTopics -> topicSwitcher.switch {
            val channel = command.channelsMap[command.channelId]?.first() as? UiChannel
                ?: throw ChannelNotFoundException()
            getChannelTopicsUseCase(command.channelId).map {
                it.toUiChannelTopics(channel.id, channel.title, channel.color)
            }.mapEvents(
                eventMapper = {
                    toUiEvent(
                        channel.copy(isCollapsed = true),
                        it,
                        command.channelsMap
                    )
                },
                errorMapper = ChannelsEvent.Internal::LoadingError
            )
        }
        is ChannelsCommand.HideChannelTopics -> topicSwitcher.switch {
            toFlow {
                val channel = command.channelsMap[command.channelId]?.firstInstance<UiChannel>()
                    ?: throw ChannelNotFoundException()

                command.channelsMap.toMutableMap().apply {
                    replace(command.channelId, listOf(channel.copy(isCollapsed = false)))
                }
            }.mapEvents(
                ChannelsEvent.Internal::LoadingChannelsWithTopicsSuccess,
                ChannelsEvent.Internal::LoadingError
            )
        }
        is ChannelsCommand.SearchChannels -> channelSwitcher.switch {
            searchChannelUseCase(
                command.query, command.filter.isSubscribed()
            ).map { it.toChannelsMap() }.mapEvents(
                ChannelsEvent.Internal::LoadingChannelsWithTopicsSuccess,
                ChannelsEvent.Internal::LoadingError
            )
        }
        is ChannelsCommand.LoadChannels -> toFlow { updateChannelsUseCase(command.filter.isSubscribed()) }.mapEvents(
            errorMapper = ChannelsEvent.Internal::LoadingError
        )
        is ChannelsCommand.LoadChannelTopic -> toFlow { loadChannelTopicsUseCase(command.channelId) }.mapEvents(
            errorMapper = ChannelsEvent.Internal::LoadingError
        )
    }

    private fun toUiEvent(
        updatedChannel: UiChannel,
        topics: List<UiChannelTopic>,
        channels: Map<Int, List<UiChannelModel>>
    ): ChannelsEvent.Internal.LoadingChannelsWithTopicsSuccess {
        val updatedMap = channels.toMutableMap().apply {
            replace(updatedChannel.id,
                mutableListOf<UiChannelModel>(updatedChannel).apply { addAll(topics) })
        }
        return ChannelsEvent.Internal.LoadingChannelsWithTopicsSuccess(updatedMap)
    }

    private fun UiChannelFilter.isSubscribed(): Boolean = when (this) {
        UiChannelFilter.ALL -> false
        UiChannelFilter.SUBSCRIBED_ONLY -> true
    }

}
