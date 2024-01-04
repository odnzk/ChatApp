package com.study.channels.presentation.channels.elm

import com.study.channels.domain.model.ChannelDoesNotHaveTopicsException
import com.study.channels.domain.model.ChannelNotFoundException
import com.study.channels.domain.model.ServerSynchronizationException
import com.study.network.model.ConnectionLostException
import com.study.network.model.NetworkException
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import javax.inject.Inject

internal class ChannelsReducer @Inject constructor() :
    DslReducer<ChannelsEvent, ChannelsState, ChannelsEffect, ChannelsCommand>() {
    override fun Result.reduce(event: ChannelsEvent) = when (event) {
        is ChannelsEvent.Ui.Init -> {
            state { copy(isLoading = true) }
            commands { +ChannelsCommand.LoadChannels(event.filter) }
        }

        is ChannelsEvent.Ui.Reload -> {
            state { copy(isLoading = true, error = null) }
            commands { +ChannelsCommand.LoadChannels(event.filter) }
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

        is ChannelsEvent.Internal.ReceivedChannelsFromDatabase -> {
            state { copy(isLoading = false, channelsWithTopics = event.channels, error = null) }
        }

        is ChannelsEvent.Internal.LoadingError -> handleError(event)
        is ChannelsEvent.Internal.ChannelsWereLoaded -> {
            commands { +ChannelsCommand.GetChannels(event.filter) }
        }

        is ChannelsEvent.Ui.Search -> {
            state { copy(isLoading = true, error = null) }
            commands { +ChannelsCommand.Search(event.filter, event.query) }
        }
    }


    private fun Result.handleError(event: ChannelsEvent.Internal.LoadingError) {
        state { copy(isLoading = false) }
        when (val error = event.error) {
            is ChannelDoesNotHaveTopicsException, is ChannelNotFoundException -> {
                effects { +ChannelsEffect.ShowWarning(error) }
            }

            is ServerSynchronizationException -> {
                effects { +ChannelsEffect.ShowSynchronizationError(event.error) }
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
