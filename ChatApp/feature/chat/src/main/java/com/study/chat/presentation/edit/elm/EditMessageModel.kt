package com.study.chat.presentation.edit.elm

import com.study.chat.domain.model.NOT_YET_SYNCHRONIZED_ID
import com.study.chat.presentation.edit.util.EditableMessage

internal data class EditMessageState(
    val messageId: Int = NOT_YET_SYNCHRONIZED_ID,
    val message: EditableMessage? = null,
    val topicsSuggestions: List<String> = emptyList()
)

internal sealed interface EditMessageEvent {
    sealed interface Ui : EditMessageEvent {
        class Init(val messageId: Int) : Ui
        class UpdateMessage(val topic: String, val content: String) : Ui
    }

    sealed interface Internal : EditMessageEvent {
        object Updated : Internal
        class MessageLoaded(val message: EditableMessage) : Internal
        class TopicSuggestionsLoaded(val topics: List<String>) : Internal
        class Error(val error: Throwable) : Internal
    }
}

internal sealed interface EditMessageEffect {
    object Success : EditMessageEffect
    class ShowError(val error: Throwable) : EditMessageEffect
}

internal sealed interface EditMessageCommand {
    class LoadMessage(val messageId: Int) : EditMessageCommand
    class LoadTopicSuggestions(val channelId: Int) : EditMessageCommand
    class UpdateMessage(val messageId: Int, val topic: String, val content: String) :
        EditMessageCommand
}

