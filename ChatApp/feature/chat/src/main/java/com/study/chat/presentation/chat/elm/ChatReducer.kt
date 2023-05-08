package com.study.chat.presentation.chat.elm

import com.study.auth.api.UserNotAuthorizedException
import com.study.chat.domain.exceptions.SynchronizationException
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import javax.inject.Inject

internal class ChatReducer @Inject constructor() :
    DslReducer<ChatEvent, ChatState, ChatEffect, ChatCommand>() {

    private var _currUserId: Int? = null
    private val currUserId get() = checkNotNull(_currUserId) { "Current user id cannot be null" }
    private val channelId get() = checkNotNull(_channelId) { "Channel id cannot be null" }
    private var _channelId: Int? = null
    private var channelTopic: String? = null

    override fun Result.reduce(event: ChatEvent) = when (event) {
        is ChatEvent.Ui.Init -> init(event)
        is ChatEvent.Internal.LoadingError ->
            when (val error = event.error) {
                is SynchronizationException, is UserNotAuthorizedException -> {
                    effects { +ChatEffect.ShowWarning(error) }
                }
                else -> state { copy(isLoading = false, error = error) }
            }
        is ChatEvent.Internal.LoadingSuccess ->
            state { copy(isLoading = false, messages = event.messages, error = null) }
        is ChatEvent.Internal.GetCurrentUserIdSuccess -> onSuccessUserIdReceiving(event)
        is ChatEvent.Ui.Reload -> {
            state { copy(isLoading = true, error = null) }
            commands { +ChatCommand.GetAllMessages(channelId, channelTopic, currUserId) }
        }
        is ChatEvent.Ui.Search -> {
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
        is ChatEvent.Ui.SendMessage -> {
            commands {
                +ChatCommand.SendMessage(
                    channelId = channelId,
                    topicTitle = event.topic,
                    messageContent = event.messageContent,
                    senderId = currUserId
                )
            }
        }
        is ChatEvent.Ui.UpdateReaction -> {
            state { copy(isLoading = true, error = null) }
            commands { +ChatCommand.UpdateReaction(event.message, event.emoji, currUserId) }
        }
        is ChatEvent.Ui.RemoveIrrelevantMessages -> channelTopic?.let {
            commands { +ChatCommand.RemoveIrrelevantMessages(channelId, it) }
        }
        ChatEvent.Internal.UpdateReactionSuccess -> state { copy(isLoading = false) }
        is ChatEvent.Internal.GettingTopicsSuccess -> state { copy(topics = event.topics) }
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
        if (channelTopic == null) {
            commands {
                +ChatCommand.LoadTopics(channelId)
                +ChatCommand.GetTopics(channelId)
            }
        }
        commands {
            +ChatCommand.GetAllMessages(
                channelId,
                channelTopic,
                currUserId
            )
        }
    }

}
