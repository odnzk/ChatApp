package com.study.chat.edit.presentation.elm

import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import javax.inject.Inject

internal class EditMessageReducer @Inject constructor() :
    DslReducer<EditMessageEvent, EditMessageState, EditMessageEffect, EditMessageCommand>() {

    override fun Result.reduce(event: EditMessageEvent): Any = when (event) {
        is EditMessageEvent.Internal.Error -> {
            effects { +EditMessageEffect.ShowError(event.error) }
        }
        EditMessageEvent.Internal.Updated -> {
            effects { +EditMessageEffect.Success }
        }
        is EditMessageEvent.Ui.Init -> {
            state { copy(messageId = event.messageId) }
            commands { +EditMessageCommand.LoadMessage(state.messageId) }
        }
        is EditMessageEvent.Ui.UpdateMessage ->
            commands {
                +EditMessageCommand.UpdateMessage(
                    state.messageId,
                    event.topic,
                    event.content
                )
            }
        is EditMessageEvent.Internal.MessageLoaded -> {
            state { copy(message = event.message) }
            commands { state.message?.channelId?.let { +EditMessageCommand.LoadTopicSuggestions(it) } }
        }
        is EditMessageEvent.Internal.TopicSuggestionsLoaded ->
            state { copy(topicsSuggestions = event.topics) }
    }
}
