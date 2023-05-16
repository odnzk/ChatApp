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
    class GetAllMessages(
        val channelId: Int,
        val topicTitle: String?,
        val userId: Int
    ) : ChatCommand

    class SendMessage(
        val channelId: Int,
        val topicTitle: String,
        val messageContent: String,
        val senderId: Int
    ) : ChatCommand

    class UpdateReaction(
        val message: UiMessage,
        val emoji: UiEmoji,
        val userId: Int
    ) : ChatCommand

    class SearchMessages(
        val channelId: Int,
        val topicTitle: String?,
        val userId: Int,
        val query: String
    ) : ChatCommand

    object GetCurrentUserId : ChatCommand
    class RemoveIrrelevantMessages(val channelId: Int, val topicTitle: String) : ChatCommand
    class LoadTopics(val channelId: Int) : ChatCommand
    class GetTopics(val channelId: Int) : ChatCommand
    class UploadFile(
        val channelId: Int,
        val topicTitle: String,
        val senderId: Int,
        val uri: String
    ) : ChatCommand
}

internal sealed interface ChatEffect {
    class ShowWarning(val error: Throwable) : ChatEffect
    object FileUploaded : ChatEffect
    class UploadingFileError(val error: Throwable) : ChatEffect
}

internal sealed interface ChatEvent {
    sealed interface Ui : ChatEvent {
        class Init(val channelId: Int, val topicTitle: String?) : Ui
        object Reload : Ui
        class SendMessage(val messageContent: String, val topic: String) : Ui
        class UploadFile(val topicTitle: String, val uri: String) : Ui
        object ReUploadFile : Ui
        class UpdateReaction(val emoji: UiEmoji, val message: UiMessage) : Ui
        class Search(val query: String) : Ui
        object RemoveIrrelevantMessages : Ui
    }

    sealed interface Internal : ChatEvent {
        class LoadingSuccess(val messages: PagingData<ChatListItem>) : Internal
        object UpdateReactionSuccess : Internal
        class LoadingError(val error: Throwable) : Internal
        class GetCurrentUserIdSuccess(val userId: Int) : Internal
        class GettingTopicsSuccess(val topics: List<String>) : Internal
        object FileUploaded : Internal
        class UploadingFileError(val error: Throwable) : Internal
    }
}
