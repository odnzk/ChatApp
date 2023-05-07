package com.study.chat.presentation.chat.elm

import androidx.paging.PagingData
import com.study.chat.domain.model.Emoji
import com.study.chat.presentation.chat.util.model.ChatListItem
import com.study.chat.presentation.chat.util.model.UiMessage

internal data class ChatState(
    val channelTitle: String? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val searchQuery: String = "",
    val messages: PagingData<ChatListItem> = PagingData.empty(),
)

internal sealed interface ChatCommand {
    class GetAllMessages(val channelTitle: String, val topicTitle: String?, val userId: Int) :
        ChatCommand

    class SendMessage(
        val channelTitle: String,
        val topicTitle: String,
        val messageContent: String,
        val senderId: Int
    ) : ChatCommand

    class UpdateReaction(
        val message: UiMessage,
        val emoji: Emoji,
        val userId: Int
    ) : ChatCommand

    class SearchMessages(
        val channelTitle: String,
        val topicTitle: String?,
        val userId: Int,
        val query: String
    ) : ChatCommand

    object GetCurrentUserId : ChatCommand

    class RemoveIrrelevantMessages(val channelTitle: String, val topicTitle: String) : ChatCommand
}

internal sealed interface ChatEffect {
    class ShowWarning(val error: Throwable) : ChatEffect
}

internal sealed interface ChatEvent {
    sealed interface Ui : ChatEvent {
        class Init(val channelTitle: String, val topicTitle: String?) : Ui
        object Reload : Ui
        class SendMessage(val messageContent: String) : Ui
        class UpdateReaction(val emoji: Emoji, val message: UiMessage) : Ui
        class Search(val query: String) : Ui
        object RemoveIrrelevantMessages : Ui
    }

    sealed interface Internal : ChatEvent {
        class LoadingSuccess(val messages: PagingData<ChatListItem>) : Internal
        object UpdateReactionSuccess : Internal
        class LoadingError(val error: Throwable) : Internal
        class GetCurrentUserIdSuccess(val userId: Int) : Internal
    }
}
