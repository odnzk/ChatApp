package com.study.channels.addChannel.presentation.elm

import com.study.channels.addChannel.domain.useCase.AddChannelUseCase
import com.study.common.extension.toFlow
import kotlinx.coroutines.flow.Flow
import vivid.money.elmslie.core.switcher.Switcher
import vivid.money.elmslie.coroutines.Actor
import vivid.money.elmslie.coroutines.switch
import javax.inject.Inject

internal class AddChannelActor @Inject constructor(private val addChannel: AddChannelUseCase) :
    Actor<AddChannelCommand, AddChannelEvent> {
    private val switcher = Switcher()

    override fun execute(command: AddChannelCommand): Flow<AddChannelEvent> =
        when (command) {
            is AddChannelCommand.AddChannel -> switcher.switch {
                toFlow { addChannel(command.title) }
            }.mapEvents(
                eventMapper = { AddChannelEvent.Internal.ChannelSuccessfullyAdded },
                errorMapper = AddChannelEvent.Internal::ChannelNotAdded
            )
        }

}
