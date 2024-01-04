package com.study.chat.chat.presentation.elm

import androidx.paging.PagingData
import com.study.chat.chat.presentation.model.ChatListItem
import com.study.chat.chat.presentation.model.UiEmoji
import com.study.chat.chat.presentation.model.UiMessage

internal data class ChatState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val searchQuery: String = "",
    val messages: PagingData<ChatListItem> = PagingData.empty(),
    val topics: List<String> = emptyList()
)

internal sealed interface ChatCommand {
    data object GetAllMessages : ChatCommand

    class SendMessage(
        val messageContent: String,
        val topic: String
    ) : ChatCommand

    class UpdateReaction(
        val message: UiMessage,
        val emoji: UiEmoji,
    ) : ChatCommand

    class RemoveIrrelevantMessages(val topic: String) : ChatCommand
    data object GetTopics : ChatCommand
    class UploadFile(
        val uri: String,
        val topic: String
    ) : ChatCommand

    class Search(val query: String) : ChatCommand
}

internal sealed interface ChatEffect {
    class ShowWarning(val error: Throwable) : ChatEffect
    data object FileUploaded : ChatEffect
    class UploadingFileError(val error: Throwable) : ChatEffect
    data object ScrollToLastItem : ChatEffect
}

internal sealed interface ChatEvent {
    sealed interface Ui : ChatEvent {
        data object Init : Ui
        data object Reload : Ui
        class SendMessage(val messageContent: String, val topic: String) : Ui
        class UploadFile(val uploadTopic: String, val uri: String) : Ui
        data object ReUploadFile : Ui
        class UpdateReaction(val emoji: UiEmoji, val message: UiMessage) : Ui
        class Search(val query: String) : Ui
        data object RemoveIrrelevantMessages : Ui
    }

    sealed interface Internal : ChatEvent {
        class LoadingSuccess(val messages: PagingData<ChatListItem>) : Internal
        class SearchSuccess(val messages: PagingData<ChatListItem>) : Internal
        data object UpdateReactionSuccess : Internal
        class Error(val error: Throwable) : Internal
        class GettingTopicsSuccess(val topics: List<String>) : Internal
        data object FileUploaded : Internal
        class UploadingFileError(val error: Throwable) : Internal
    }
}
