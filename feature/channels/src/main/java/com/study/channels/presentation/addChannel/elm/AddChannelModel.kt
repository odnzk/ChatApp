package com.study.channels.presentation.addChannel.elm

internal data class AddChannelState(
    val isLoading: Boolean = false,
    val successfullyAdded: Boolean = false,
    val error: Throwable? = null
)

internal sealed interface AddChannelEvent {
    sealed interface Ui : AddChannelEvent {
        data object Init : Ui
        class AddChannel(val title: String) : Ui
    }

    sealed interface Internal : AddChannelEvent {
        data object ChannelSuccessfullyAdded : Internal
        class ChannelNotAdded(val error: Throwable) : Internal
    }
}

internal sealed interface AddChannelEffect
internal sealed interface AddChannelCommand {
    class AddChannel(val title: String) : AddChannelCommand
}
