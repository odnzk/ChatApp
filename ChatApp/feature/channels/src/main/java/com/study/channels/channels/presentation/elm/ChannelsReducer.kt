package com.study.channels.channels.presentation.elm

import com.study.channels.shared.domain.model.ChannelDoesNotHaveTopicsException
import com.study.channels.shared.domain.model.ChannelNotFoundException
import com.study.network.model.ConnectionLostException
import com.study.network.model.NetworkException
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import javax.inject.Inject

internal class ChannelsReducer @Inject constructor() :
    DslReducer<ChannelsEvent, ChannelsState, ChannelsEffect, ChannelsCommand>() {
    override fun Result.reduce(event: ChannelsEvent) = when (event) {
        is ChannelsEvent.Ui.Init -> if (state.channelsWithTopics.isEmpty()) {
            state { copy(isLoading = true, channelFilter = event.channelFilter) }
            commands {
                +ChannelsCommand.GetChannels(state.channelFilter)
                +ChannelsCommand.LoadChannels(state.channelFilter)
            }
        } else Unit
        ChannelsEvent.Ui.Reload -> {
            state { copy(isLoading = true, error = null) }
            commands {
                +ChannelsCommand.GetChannels(state.channelFilter)
                +ChannelsCommand.LoadChannels(state.channelFilter)
            }
        }
        is ChannelsEvent.Ui.ManageChannelTopics -> {
            state { copy(isLoading = true, error = null) }
            if (event.isCollapsed)
                commands {
                    +ChannelsCommand.HideChannelTopics(event.channelId, state.channelsWithTopics)
                }
            else
                commands {
                    +ChannelsCommand.LoadChannelTopic(event.channelId)
                    +ChannelsCommand.ShowChannelTopics(event.channelId, state.channelsWithTopics)
                }
        }
        is ChannelsEvent.Ui.Search -> {
            state { copy(isLoading = true, error = null) }
            commands { +ChannelsCommand.SearchChannels(event.query, state.channelFilter) }
        }
        is ChannelsEvent.Internal.LoadingChannelsWithTopicsSuccess -> {
            state { copy(isLoading = false, channelsWithTopics = event.channels, error = null) }
        }
        is ChannelsEvent.Internal.LoadingError -> {
            state { copy(isLoading = false) }
            when (val error = event.error) {
                is ChannelDoesNotHaveTopicsException, is ChannelNotFoundException -> {
                    effects { +ChannelsEffect.ShowWarning(error) }
                }
                is ConnectionLostException, is NetworkException -> {
                    if (state.channelsWithTopics.isNotEmpty()) {
                        effects { +ChannelsEffect.ShowWarning(error) }
                    } else {
                        state { copy(error = error) }
                    }
                }
                else -> state { copy(error = error) }
            }
        }
    }
}
