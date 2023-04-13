package com.study.channels.presentation.elm

import com.study.channels.domain.exceptions.ChannelDoesNotHaveTopicsException
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

internal class ChannelsReducer :
    DslReducer<ChannelsEvent, ChannelsState, ChannelsEffect, ChannelsCommand>() {

    override fun Result.reduce(event: ChannelsEvent) = when (event) {
        is ChannelsEvent.Ui.Init -> {
            state { copy(isLoading = true) }
            commands { +ChannelsCommand.LoadChannels(state.channelFilter) }
        }
        ChannelsEvent.Ui.Reload -> {
            state { copy(isLoading = true) }
            commands { +ChannelsCommand.LoadChannels(state.channelFilter) }
        }
        is ChannelsEvent.Ui.ManageChannelTopics -> {
            state { copy(isLoading = true) }
            commands {
                +ChannelsCommand.ManageChannelTopics(event.channelId, state.channelsWithTopics)
            }
        }
        is ChannelsEvent.Ui.Search -> {
            state { copy(isLoading = true) }
            commands { +ChannelsCommand.SearchChannels(event.query, state.channelFilter) }
        }
        is ChannelsEvent.Internal.LoadingChannelsWithTopicsSuccess -> {
            state { copy(isLoading = false, channelsWithTopics = event.channels) }
        }
        is ChannelsEvent.Internal.LoadingError -> {
            val error = event.error
            effects {
                if (error is ChannelDoesNotHaveTopicsException) +ChannelsEffect.ShowWarning(error)
                else +ChannelsEffect.ShowError(error)
            }
        }
    }
}
