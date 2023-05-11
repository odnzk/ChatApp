package com.study.channels.presentation.addChannel.elm

import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import javax.inject.Inject

internal class AddChannelReducer @Inject constructor() :
    DslReducer<AddChannelEvent, AddChannelState, AddChannelEffect, AddChannelCommand>() {
    override fun Result.reduce(event: AddChannelEvent): Any = when (event) {
        AddChannelEvent.Ui.Init -> Unit
        is AddChannelEvent.Internal.ChannelNotAdded ->
            state { copy(error = event.error, isLoading = false) }
        is AddChannelEvent.Internal.ChannelSuccessfullyAdded -> {
            state { copy(successfullyAdded = true, error = null, isLoading = false) }
        }
        is AddChannelEvent.Ui.AddChannel -> {
            state { copy(isLoading = true) }
            commands { +AddChannelCommand.AddChannel(event.title) }
        }
    }
}
