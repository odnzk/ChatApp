package com.study.chat.actions.presentation.elm

import com.study.chat.actions.presentation.model.UiAction
import com.study.chat.shared.domain.model.SynchronizationException
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import javax.inject.Inject

internal class ActionsReducer @Inject constructor() :
    DslReducer<ActionsEvent, ActionsState, ActionsEffect, ActionsCommand>() {

    override fun Result.reduce(event: ActionsEvent): Any = when (event) {
        is ActionsEvent.Internal.Error -> {
            state { copy(isLoading = false) }
            when (val error = event.error) {
                is SynchronizationException -> effects {
                    +ActionsEffect.ShowSynchronizationError(error)
                }
                else -> effects { +ActionsEffect.ShowError(error) }
            }
        }
        is ActionsEvent.Ui.CopyMessage -> {
            state { copy(isLoading = true) }
            commands { +ActionsCommand.CopyToClipboard(event.messageId) }
        }
        is ActionsEvent.Ui.DeleteMessage -> {
            state { copy(isLoading = true) }
            commands { +ActionsCommand.DeleteMessage(event.messageId) }
        }
        is ActionsEvent.Internal.SuccessfullyReceivedUserRole -> {
            state { copy(isLoading = false, userRole = event.userRole) }
        }
        is ActionsEvent.Ui.Init -> {
            state { copy(isLoading = true) }
            commands { +ActionsCommand.GetUserRole(event.messageId) }
        }
        ActionsEvent.Internal.MessageDeleted -> {
            state { copy(isLoading = false) }
            effects { +ActionsEffect.SuccessAction(UiAction.DELETE) }
        }
        ActionsEvent.Internal.Copied -> {
            state { copy(isLoading = false) }
            effects { +ActionsEffect.SuccessAction(UiAction.COPY) }
        }
    }

}
