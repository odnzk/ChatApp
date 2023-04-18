package com.study.chat.presentation.chat.elm

import androidx.paging.PagingData
import com.study.chat.domain.model.Emoji
import com.study.chat.presentation.chat.util.model.UiMessage

internal data class ChatState(
    val channelTitle: String = "",
    val channelTopicTitle: String = "",
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val searchQuery: String = "",
    val messages: PagingData<Any> = PagingData.empty(),
)

internal sealed interface ChatCommand {
    class GetAllMessages(val channelTitle: String, val topicTitle: String, val userId: Int) :
        ChatCommand

    class SendMessage(
        val channelTitle: String,
        val topicTitle: String,
        val currMessages: List<Any>,
        val messageContent: String
    ) : ChatCommand

    class UpdateReaction(
        val message: UiMessage,
        val emoji: Emoji,
        val currMessages: List<Any>
    ) : ChatCommand

    class SearchMessages(
        val channelTitle: String,
        val topicTitle: String,
        val userId: Int,
        val query: String
    ) : ChatCommand

    object GetCurrentUserId : ChatCommand
}

internal sealed interface ChatEffect {
    class ShowWarning(val error: Throwable) : ChatEffect
}

internal sealed interface ChatEvent {
    sealed interface Ui : ChatEvent {
        class Init(val channelTitle: String, val channelTopicTitle: String) : Ui
        object Reload : Ui
        class SendMessage(val messageContent: String, val messages: List<Any?>) : Ui
        class UpdateReaction(
            val message: UiMessage,
            val emoji: Emoji,
            val messages: List<Any?>
        ) : Ui

        class Search(val query: String) : Ui
    }

    sealed interface Internal : ChatEvent {
        class LoadingSuccess(val messages: PagingData<Any>) : Internal
        class LoadingError(val error: Throwable) : Internal
        class GetCurrentUserIdSuccess(val userId: Int) : Internal
    }
}
