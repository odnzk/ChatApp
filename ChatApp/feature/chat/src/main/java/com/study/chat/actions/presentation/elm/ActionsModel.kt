package com.study.chat.actions.presentation.elm

import com.study.chat.actions.presentation.model.UiAction
import com.study.chat.actions.presentation.model.UiUserRole

internal data class ActionsState(
    val isLoading: Boolean = false,
    val userRole: UiUserRole = UiUserRole.UNDEFINED
)

internal sealed interface ActionsEvent {
    sealed interface Ui : ActionsEvent {
        class Init(val messageId: Int) : Ui
        class DeleteMessage(val messageId: Int) : Ui
        class CopyMessage(val messageId: Int) : Ui
    }

    sealed interface Internal : ActionsEvent {
        object MessageDeleted : Internal
        object Copied : Internal
        class SuccessfullyReceivedUserRole(val userRole: UiUserRole) : Internal
        class Error(val error: Throwable) : Internal
    }
}

internal sealed interface ActionsEffect {
    class SuccessAction(val action: UiAction) : ActionsEffect
    class ShowError(val error: Throwable) : ActionsEffect
    class ShowSynchronizationError(val error: Throwable) : ActionsEffect
}

internal sealed interface ActionsCommand {
    class GetUserRole(val messageId: Int) : ActionsCommand
    class DeleteMessage(val messageId: Int) : ActionsCommand
    class CopyToClipboard(val messageId: Int) : ActionsCommand
}

