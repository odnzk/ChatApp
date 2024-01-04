package com.study.chat.edit.presentation.elm

import com.study.chat.edit.domain.useCase.GetMessageUseCase
import com.study.chat.edit.domain.useCase.UpdateMessageUseCase
import com.study.chat.edit.presentation.util.toEditableMessage
import com.study.chat.common.domain.usecase.GetTopicsUseCase
import com.study.common.ext.toFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.switcher.Switcher
import vivid.money.elmslie.coroutines.Actor
import vivid.money.elmslie.coroutines.switch
import javax.inject.Inject

internal class EditMessageActor @Inject constructor(
    private val getTopicsUseCase: GetTopicsUseCase,
    private val getMessageUseCase: GetMessageUseCase,
    private val updateMessageUseCase: UpdateMessageUseCase
) : Actor<EditMessageCommand, EditMessageEvent> {

    private val switcher = Switcher()
    override fun execute(command: EditMessageCommand): Flow<EditMessageEvent> = when (command) {
        is EditMessageCommand.UpdateMessage -> switcher.switch {
            toFlow {
                updateMessageUseCase(command.messageId, command.content, command.topic)
            }.mapEvents(
                eventMapper = { EditMessageEvent.Internal.Updated },
                errorMapper = EditMessageEvent.Internal::Error
            )
        }

        is EditMessageCommand.LoadMessage -> toFlow {
            getMessageUseCase(command.messageId).toEditableMessage()
        }.mapEvents(
            EditMessageEvent.Internal::MessageLoaded,
            EditMessageEvent.Internal::Error
        )

        is EditMessageCommand.LoadTopicSuggestions -> flow {
            emit(getTopicsUseCase(command.channelId))
        }
            .mapEvents(
                EditMessageEvent.Internal::TopicSuggestionsLoaded,
                EditMessageEvent.Internal::Error
            )
    }
}
