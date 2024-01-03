package com.study.channels.channels.presentation.elm

import com.study.channels.channels.domain.usecase.GetChannelTopicsUseCase
import com.study.channels.channels.domain.usecase.GetChannelsUseCase
import com.study.channels.channels.domain.usecase.LoadChannelTopicsUseCase
import com.study.channels.channels.domain.usecase.SearchChannelUseCase
import com.study.channels.channels.domain.usecase.UpdateChannelsUseCase
import com.study.channels.channels.presentation.elm.ChannelsEvent.Internal.ChannelsWereLoaded
import com.study.channels.channels.presentation.model.UiChannel
import com.study.channels.channels.presentation.model.UiChannelModel
import com.study.channels.channels.presentation.model.UiChannelTopic
import com.study.channels.channels.presentation.util.mapper.toChannelsMap
import com.study.channels.channels.presentation.util.mapper.toUiChannelTopics
import com.study.channels.common.domain.model.ChannelNotFoundException
import com.study.channels.common.domain.model.ServerSynchronizationException
import com.study.channels.common.domain.model.notYetSynchronizedChannelId
import com.study.common.ext.firstInstance
import com.study.common.ext.toFlow
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
            getChannelsUseCase(command.filter.isSubscribed())
                .map(::toChannelsMap)
                .mapEvents(
                    ChannelsEvent.Internal::ReceivedChannelsFromDatabase,
                    ChannelsEvent.Internal::LoadingError
                )
        }

        is ChannelsCommand.ShowChannelTopics -> topicSwitcher.switch {
            val channel = command.channelsMap.findChannel(command.channelId)

            getChannelTopicsUseCase(command.channelId)
                .map { it.toUiChannelTopics(channel.id, channel.title, channel.color) }
                .mapEvents(
                    eventMapper = {
                        toUiEvent(channel.copy(isCollapsed = true), it, command.channelsMap)
                    }, errorMapper = ChannelsEvent.Internal::LoadingError
                )
        }

        is ChannelsCommand.HideChannelTopics -> topicSwitcher.switch {
            toFlow {
                val channel = command.channelsMap.findChannel(command.channelId)

                command.channelsMap
                    .toMutableMap()
                    .apply { replace(command.channelId, listOf(channel.copy(isCollapsed = false))) }
            }.mapEvents(
                ChannelsEvent.Internal::ReceivedChannelsFromDatabase,
                ChannelsEvent.Internal::LoadingError
            )
        }

        is ChannelsCommand.LoadChannels -> toFlow {
            updateChannelsUseCase(command.filter.isSubscribed())
        }.mapEvents(
            errorMapper = ChannelsEvent.Internal::LoadingError,
            eventMapper = { ChannelsWereLoaded(command.filter) })

        is ChannelsCommand.LoadChannelTopic -> toFlow {
            if (command.channelId in notYetSynchronizedChannelId) throw ServerSynchronizationException()
            loadChannelTopicsUseCase(command.channelId)
        }.mapEvents(errorMapper = ChannelsEvent.Internal::LoadingError)

        is ChannelsCommand.Search -> channelSwitcher.switch {
            searchChannelUseCase(command.query, command.filter.isSubscribed())
                .map(::toChannelsMap)
                .mapEvents(
                    ChannelsEvent.Internal::ReceivedChannelsFromDatabase,
                    ChannelsEvent.Internal::LoadingError
                )
        }
    }

    private fun toUiEvent(
        updatedChannel: UiChannel,
        topics: List<UiChannelTopic>,
        channels: Map<Int, List<UiChannelModel>>
    ): ChannelsEvent.Internal.ReceivedChannelsFromDatabase {
        val updatedMap = channels
            .toMutableMap()
            .apply {
                replace(updatedChannel.id,
                    mutableListOf<UiChannelModel>(updatedChannel).apply { addAll(topics) })
            }
        return ChannelsEvent.Internal.ReceivedChannelsFromDatabase(updatedMap)
    }


    private fun Map<Int, List<UiChannelModel>>.findChannel(channelId: Int): UiChannel {
        val channel = get(channelId)?.firstInstance<UiChannel>() ?: throw ChannelNotFoundException()
        if (channel.id in notYetSynchronizedChannelId) throw ServerSynchronizationException()
        return channel
    }

}
