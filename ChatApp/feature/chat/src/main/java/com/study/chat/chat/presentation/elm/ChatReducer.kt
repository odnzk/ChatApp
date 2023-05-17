package com.study.chat.chat.presentation.elm

import com.study.auth.api.UserNotAuthorizedException
import com.study.chat.shared.domain.model.SynchronizationException
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import javax.inject.Inject

internal class ChatReducer @Inject constructor() :
    DslReducer<ChatEvent, ChatState, ChatEffect, ChatCommand>() {

    private var _currUserId: Int? = null
    private val currUserId get() = checkNotNull(_currUserId) { "Current user id cannot be null" }
    private val channelId get() = checkNotNull(_channelId) { "Channel id cannot be null" }
    private var _channelId: Int? = null
    private var channelTopic: String? = null
    private var prevUploadedUriWithTopic: Pair<String, String>? = null

    override fun Result.reduce(event: ChatEvent) = when (event) {
        is ChatEvent.Ui.Init -> init(event)
        is ChatEvent.Internal.LoadingError -> handleError(event)
        is ChatEvent.Internal.GetCurrentUserIdSuccess -> onSuccessUserIdReceiving(event)
        is ChatEvent.Ui.Search -> search(event)
        is ChatEvent.Ui.SendMessage -> sendMessage(event)
        is ChatEvent.Ui.UploadFile -> {
            prevUploadedUriWithTopic = event.uri to event.topicTitle
            uploadFile(event.topicTitle, event.uri)
        }
        ChatEvent.Ui.ReUploadFile -> prevUploadedUriWithTopic?.let {
            uploadFile(it.second, it.first)
        }
        is ChatEvent.Internal.LoadingSuccess ->
            state { copy(isLoading = false, messages = event.messages, error = null) }
        is ChatEvent.Ui.Reload -> {
            state { copy(isLoading = true, error = null) }
            commands { +ChatCommand.GetAllMessages(channelId, channelTopic, currUserId) }
        }
        is ChatEvent.Ui.UpdateReaction -> {
            state { copy(isLoading = true, error = null) }
            commands { +ChatCommand.UpdateReaction(event.message, event.emoji, currUserId) }
        }
        is ChatEvent.Ui.RemoveIrrelevantMessages -> channelTopic?.let {
            commands { +ChatCommand.RemoveIrrelevantMessages(channelId, it) }
        }
        ChatEvent.Internal.UpdateReactionSuccess -> state { copy(isLoading = false, error = null) }
        is ChatEvent.Internal.GettingTopicsSuccess -> state { copy(topics = event.topics) }
        ChatEvent.Internal.FileUploaded -> {
            state { copy(isLoading = false, error = null) }
            effects { +ChatEffect.FileUploaded }
        }
        is ChatEvent.Internal.UploadingFileError -> {
            state { copy(isLoading = false) }
            effects { +ChatEffect.UploadingFileError(event.error) }
        }
    }

    private fun Result.init(event: ChatEvent.Ui.Init) =
        if (_currUserId == null) {
            _channelId = event.channelId
            channelTopic = event.topicTitle
            state { copy(isLoading = true) }
            commands { +ChatCommand.GetCurrentUserId }
        } else Unit

    private fun Result.onSuccessUserIdReceiving(event: ChatEvent.Internal.GetCurrentUserIdSuccess) {
        _currUserId = event.userId
        commands {
            +ChatCommand.GetAllMessages(
                channelId,
                channelTopic,
                currUserId
            )
            if (channelTopic == null) {
                +ChatCommand.LoadTopics(channelId)
                +ChatCommand.GetTopics(channelId)
            }
        }
    }

    private fun Result.handleError(event: ChatEvent.Internal.LoadingError) {
        state { copy(isLoading = false) }
        when (val error = event.error) {
            is SynchronizationException, is UserNotAuthorizedException -> {
                effects { +ChatEffect.ShowWarning(error) }
            }
            else -> state { copy(error = error) }
        }
    }

    private fun Result.uploadFile(topic: String, uri: String) {
        state { copy(isLoading = true, error = null) }
        commands {
            +ChatCommand.UploadFile(
                channelId = channelId,
                topicTitle = topic,
                uri = uri,
                senderId = currUserId
            )
        }
    }

    private fun Result.search(event: ChatEvent.Ui.Search) {
        state { copy(isLoading = true, error = null) }
        commands {
            +ChatCommand.SearchMessages(
                channelId = channelId,
                topicTitle = channelTopic,
                userId = currUserId,
                query = event.query
            )
        }
    }

    private fun Result.sendMessage(event: ChatEvent.Ui.SendMessage) {
        state { copy(isLoading = true) }
        commands {
            +ChatCommand.SendMessage(
                channelId = channelId,
                topicTitle = event.topic,
                messageContent = event.messageContent,
                senderId = currUserId
            )
        }
    }

}
