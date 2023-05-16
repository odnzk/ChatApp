package com.study.chat.actions.presentation.elm

import com.study.chat.actions.domain.usecase.CopyMessageUseCase
import com.study.chat.actions.domain.usecase.DeleteMessageUseCase
import com.study.chat.actions.domain.usecase.GetUserRoleUseCase
import com.study.chat.actions.presentation.util.mapper.toUiUserRole
import com.study.common.extension.toFlow
import kotlinx.coroutines.flow.Flow
import vivid.money.elmslie.coroutines.Actor
import javax.inject.Inject

internal class ActionsActor @Inject constructor(
    private val copyMessageUseCase: CopyMessageUseCase,
    private val getUserRoleUseCase: GetUserRoleUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase
) : Actor<ActionsCommand, ActionsEvent> {

    override fun execute(command: ActionsCommand): Flow<ActionsEvent> = when (command) {
        is ActionsCommand.DeleteMessage -> toFlow {
            deleteMessageUseCase(command.messageId)
        }.mapEvents(
            eventMapper = { ActionsEvent.Internal.MessageDeleted },
            errorMapper = ActionsEvent.Internal::Error
        )
        is ActionsCommand.GetUserRole -> toFlow {
            getUserRoleUseCase(command.messageId).toUiUserRole()
        }.mapEvents(
            eventMapper = ActionsEvent.Internal::SuccessfullyReceivedUserRole,
            errorMapper = ActionsEvent.Internal::Error
        )
        is ActionsCommand.CopyToClipboard -> toFlow {
            copyMessageUseCase(command.messageId)
        }.mapEvents(
            eventMapper = { ActionsEvent.Internal.Copied },
            errorMapper = ActionsEvent.Internal::Error
        )
    }
}
