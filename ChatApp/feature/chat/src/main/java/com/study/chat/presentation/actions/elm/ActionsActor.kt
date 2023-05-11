package com.study.chat.presentation.actions.elm

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.study.chat.domain.usecase.DeleteMessageUseCase
import com.study.chat.domain.usecase.GetMessageUseCase
import com.study.chat.domain.usecase.GetUserRoleUseCase
import com.study.chat.presentation.actions.util.mapper.toUiUserRole
import com.study.common.extension.toFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.coroutines.Actor
import javax.inject.Inject

internal class ActionsActor @Inject constructor(
    private val applicationContext: Context,
    private val getUserRoleUseCase: GetUserRoleUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val getMessageUseCase: GetMessageUseCase
) : Actor<ActionsCommand, ActionsEvent> {

    override fun execute(command: ActionsCommand): Flow<ActionsEvent> = when (command) {
        is ActionsCommand.DeleteMessage -> toFlow {
            deleteMessageUseCase(command.messageId)
        }.mapEvents(
            eventMapper = { ActionsEvent.Internal.MessageDeleted },
            errorMapper = ActionsEvent.Internal::Error
        )
        is ActionsCommand.GetUserRole -> flow {
            emit(getUserRoleUseCase(command.messageId).toUiUserRole())
        }.mapEvents(
            eventMapper = ActionsEvent.Internal::SuccessfullyReceivedUserRole,
            errorMapper = ActionsEvent.Internal::Error
        )
        is ActionsCommand.CopyToClipboard -> toFlow {
            val content = getMessageUseCase(command.messageId).content
            val manager = applicationContext.getSystemService(ClipboardManager::class.java)
            ClipData.newPlainText(content.createLabel(), content).also { clipData ->
                manager.setPrimaryClip(clipData)
            }
        }.mapEvents(
            eventMapper = { ActionsEvent.Internal.Copied },
            errorMapper = ActionsEvent.Internal::Error
        )
    }


    private fun String.createLabel(): String = this.take(LABEL_LENGTH).plus("...")

    companion object {
        private const val LABEL_LENGTH = 10
    }
}
