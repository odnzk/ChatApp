package com.study.chat.chat.presentation.elm

import com.study.auth.api.UserNotAuthorizedException
import com.study.chat.common.domain.model.SynchronizationException
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

internal class ChatReducer @AssistedInject constructor(
    @Assisted("topicTitle") private val topicTitle: String?
) : DslReducer<ChatEvent, ChatState, ChatEffect, ChatCommand>() {

    private var prevUploadedUriWithTopic: Pair<String, String>? = null

    override fun Result.reduce(event: ChatEvent) = when (event) {
        is ChatEvent.Ui.Init -> init()
        is ChatEvent.Internal.Error -> showError(event)
        is ChatEvent.Ui.Search -> search(event)
        is ChatEvent.Ui.SendMessage -> sendMessage(event)
        is ChatEvent.Ui.UploadFile -> uploadFile(event)
        ChatEvent.Ui.ReUploadFile -> reUploadFile()
        is ChatEvent.Internal.LoadingSuccess -> showSuccess(event)
        is ChatEvent.Ui.Reload -> reload()
        is ChatEvent.Ui.UpdateReaction -> updateReaction(event)
        is ChatEvent.Ui.RemoveIrrelevantMessages -> removeIrrelevantMessages()
        ChatEvent.Internal.UpdateReactionSuccess -> reactionWasUpdated()
        is ChatEvent.Internal.GettingTopicsSuccess -> showTopics(event)
        ChatEvent.Internal.FileUploaded -> fileWasUploaded()
        is ChatEvent.Internal.UploadingFileError -> errorWhileUploadingFile(event)
        is ChatEvent.Internal.SearchSuccess -> showSearchSuccess(event)
    }

    private fun Result.showSearchSuccess(event: ChatEvent.Internal.SearchSuccess) {
        state { copy(isLoading = false, messages = event.messages, error = null) }
        effects { +ChatEffect.ScrollToLastItem }
    }

    private fun Result.showError(event: ChatEvent.Internal.Error) {
        state { copy(isLoading = false) }
        when (val error = event.error) {
            is SynchronizationException, is UserNotAuthorizedException -> {
                effects { +ChatEffect.ShowWarning(error) }
            }

            else -> state { copy(error = error) }
        }
    }

    private fun Result.applyUploadFileState(topic: String, uri: String) {
        state { copy(isLoading = true, error = null) }
        commands {
            +ChatCommand.UploadFile(uri = uri, topic = topic)
        }
    }

    private fun Result.uploadFile(event: ChatEvent.Ui.UploadFile) {
        prevUploadedUriWithTopic = event.uri to event.uploadTopic
        applyUploadFileState(event.uploadTopic, event.uri)
    }

    private fun Result.reUploadFile() {
        prevUploadedUriWithTopic?.let { applyUploadFileState(it.second, it.first) }
    }

    private fun Result.search(event: ChatEvent.Ui.Search) {
        commands { +ChatCommand.Search(event.query) }
    }

    private fun Result.sendMessage(event: ChatEvent.Ui.SendMessage) {
        state { copy(isLoading = true) }
        commands {
            +ChatCommand.SendMessage(
                messageContent = event.messageContent,
                event.topic
            )
        }
    }

    private fun Result.init() {
        state { copy(isLoading = true) }
        commands {
            +ChatCommand.GetAllMessages
            if (topicTitle == null) {
                +ChatCommand.GetTopics
            }
        }
    }

    private fun Result.showSuccess(event: ChatEvent.Internal.LoadingSuccess) {
        state { copy(isLoading = false, messages = event.messages, error = null) }
    }

    private fun Result.reload() {
        state { copy(isLoading = true, error = null) }
        commands { +ChatCommand.GetAllMessages }
    }

    private fun Result.updateReaction(event: ChatEvent.Ui.UpdateReaction) {
        state { copy(isLoading = true, error = null) }
        commands { +ChatCommand.UpdateReaction(message = event.message, emoji = event.emoji) }
    }

    private fun Result.removeIrrelevantMessages() {
        topicTitle?.let {
            commands { +ChatCommand.RemoveIrrelevantMessages(topicTitle) }
        }
    }

    private fun Result.reactionWasUpdated() {
        state { copy(isLoading = false, error = null) }
    }

    private fun Result.showTopics(event: ChatEvent.Internal.GettingTopicsSuccess) {
        state { copy(topics = event.topics) }
    }

    private fun Result.fileWasUploaded() {
        state { copy(isLoading = false, error = null) }
        effects { +ChatEffect.FileUploaded }
    }

    private fun Result.errorWhileUploadingFile(event: ChatEvent.Internal.UploadingFileError) {
        state { copy(isLoading = false) }
        effects { +ChatEffect.UploadingFileError(event.error) }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("topicTitle") topicTitle: String?
        ): ChatReducer
    }
}
