package com.study.chat.presentation.edit.elm

import com.study.chat.domain.usecase.GetMessageUseCase
import com.study.chat.domain.usecase.GetTopicsUseCase
import com.study.chat.domain.usecase.UpdateMessageUseCase
import com.study.chat.presentation.edit.util.mapper.toEditableMessage
import com.study.common.extension.toFlow
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
            flow {
                emit(updateMessageUseCase(command.messageId, command.content, command.topic))
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
        is EditMessageCommand.LoadTopicSuggestions -> getTopicsUseCase(command.channelId)
            .mapEvents(
                EditMessageEvent.Internal::TopicSuggestionsLoaded,
                EditMessageEvent.Internal::Error
            )
    }
}
