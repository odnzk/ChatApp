package com.study.chat.presentation.actions.elm

import com.study.chat.domain.model.NOT_YET_SYNCHRONIZED_ID
import com.study.chat.presentation.actions.util.model.UiAction
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import javax.inject.Inject

internal class ActionsReducer @Inject constructor() :
    DslReducer<ActionsEvent, ActionsState, ActionsEffect, ActionsCommand>() {

    override fun Result.reduce(event: ActionsEvent): Any = when (event) {
        is ActionsEvent.Internal.Error -> {
            effects { +ActionsEffect.ShowError(event.error) }
        }
        is ActionsEvent.Ui.CopyMessage -> {
            commands { +ActionsCommand.CopyToClipboard(event.messageId) }
        }
        is ActionsEvent.Ui.DeleteMessage -> {
            commands { +ActionsCommand.DeleteMessage(event.messageId) }
        }
        is ActionsEvent.Internal.SuccessfullyReceivedUserRole -> {
            state { copy(userRole = event.userRole) }
        }
        is ActionsEvent.Ui.Init -> {
            if (event.messageId == NOT_YET_SYNCHRONIZED_ID) {
                effects { +ActionsEffect.ShowSynchronizationError }
            } else {
                commands { +ActionsCommand.GetUserRole(event.messageId) }
            }
        }
        ActionsEvent.Internal.MessageDeleted -> {
            effects { +ActionsEffect.SuccessAction(UiAction.DELETE) }
        }
        ActionsEvent.Internal.Copied -> {
            effects { +ActionsEffect.SuccessAction(UiAction.COPY) }
        }
    }

}
