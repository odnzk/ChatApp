package com.study.chat.presentation.chat.elm

import com.study.auth.api.UserNotAuthorizedException
import com.study.chat.domain.exceptions.SynchronizationException
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import javax.inject.Inject

internal class ChatReducer @Inject constructor() :
    DslReducer<ChatEvent, ChatState, ChatEffect, ChatCommand>() {

    private var _currUserId: Int? = null
    private val currUserId get() = checkNotNull(_currUserId) { "Current user id cannot be null" }
    private var _channelTitle: String? = null
    private val channelTitle get() = checkNotNull(_channelTitle) { "Channel title cannot be null" }
    private var channelTopic: String? = null

    override fun Result.reduce(event: ChatEvent) = when (event) {
        is ChatEvent.Ui.Init -> if (_currUserId == null) {
            channelTopic = event.topicTitle
            _channelTitle = event.channelTitle
            state { copy(isLoading = true) }
            commands { +ChatCommand.GetCurrentUserId }
        } else Unit
        is ChatEvent.Internal.LoadingError ->
            when (val error = event.error) {
                is SynchronizationException, is UserNotAuthorizedException -> {
                    effects { +ChatEffect.ShowWarning(error) }
                }
                else -> state { copy(isLoading = false, error = error) }
            }
        is ChatEvent.Internal.LoadingSuccess ->
            state { copy(isLoading = false, messages = event.messages, error = null) }
        is ChatEvent.Internal.GetCurrentUserIdSuccess -> {
            _currUserId = event.userId
            commands {
                +ChatCommand.GetAllMessages(
                    channelTitle,
                    channelTopic,
                    currUserId
                )
            }
        }
        is ChatEvent.Ui.Reload -> {
            state { copy(isLoading = true, error = null) }
            commands { +ChatCommand.GetAllMessages(channelTitle, channelTopic, currUserId) }
        }
        is ChatEvent.Ui.Search -> {
            state { copy(isLoading = true, error = null) }
            commands {
                +ChatCommand.SearchMessages(
                    channelTitle = channelTitle,
                    topicTitle = channelTopic,
                    userId = currUserId,
                    query = event.query
                )
            }
        }
        is ChatEvent.Ui.SendMessage -> {
            commands {
                +ChatCommand.SendMessage(
                    channelTitle = channelTitle,
                    // todo provide topic if we are in channel
                    topicTitle = channelTopic.orEmpty(),
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
            commands { +ChatCommand.RemoveIrrelevantMessages(channelTitle, it) }
        }
        ChatEvent.Internal.UpdateReactionSuccess -> state { copy(isLoading = false) }
    }
}
