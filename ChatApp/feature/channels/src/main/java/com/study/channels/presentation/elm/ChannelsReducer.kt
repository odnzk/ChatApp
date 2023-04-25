package com.study.channels.presentation.elm

import com.study.channels.domain.exceptions.ChannelDoesNotHaveTopicsException
import com.study.channels.domain.exceptions.ChannelNotFoundException
import retrofit2.HttpException
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import javax.inject.Inject

internal class ChannelsReducer @Inject constructor() :
    DslReducer<ChannelsEvent, ChannelsState, ChannelsEffect, ChannelsCommand>() {
    override fun Result.reduce(event: ChannelsEvent) = when (event) {
        is ChannelsEvent.Ui.Init -> if (state.channelsWithTopics.isEmpty()) {
            state { copy(isLoading = true, channelFilter = event.channelFilter) }
            commands {
                +ChannelsCommand.LoadChannels(state.channelFilter)
                +ChannelsCommand.UpdateChannels(state.channelFilter)
            }
        } else Unit
        ChannelsEvent.Ui.Reload -> {
            state { copy(isLoading = true, error = null) }
            commands {
                +ChannelsCommand.LoadChannels(state.channelFilter)
                +ChannelsCommand.UpdateChannels(state.channelFilter)
            }
        }
        is ChannelsEvent.Ui.ManageChannelTopics -> {
            state { copy(isLoading = true, error = null) }
            commands {
                +ChannelsCommand.UpdateChannelTopic(event.channelId)
                +ChannelsCommand.ManageChannelTopics(event.channelId, state.channelsWithTopics)
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
            when (val error = event.error) {
                is ChannelDoesNotHaveTopicsException, is ChannelNotFoundException, is HttpException -> {
                    state { copy(isLoading = false) }
                    effects { +ChannelsEffect.ShowWarning(error) }
                }
                else -> state { copy(isLoading = false, error = error) }
            }
        }
    }
}
