package com.study.chat.presentation.chat.elm

import com.study.auth.api.UserNotAuthorizedException
import com.study.chat.domain.exceptions.ContentHasNotLoadedException
import com.study.chat.domain.exceptions.SynchronizationException
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import javax.inject.Inject


internal class ChatReducer @Inject constructor() :
    DslReducer<ChatEvent, ChatState, ChatEffect, ChatCommand>() {

    private var _currUserId: Int? = null
    private val currUserId get() = checkNotNull(_currUserId) { "Current user id cannot be null" }
    override fun Result.reduce(event: ChatEvent) = when (event) {
        is ChatEvent.Ui.Init -> if (state.channelTitle.isEmpty()) {
            state {
                copy(
                    isLoading = true,
                    channelTopicTitle = event.channelTopicTitle,
                    channelTitle = event.channelTitle
                )
            }
            commands { +ChatCommand.GetCurrentUserId }
        } else Unit
        is ChatEvent.Internal.LoadingError ->
            if (event.error is SynchronizationException || event.error is UserNotAuthorizedException) {
                effects { +ChatEffect.ShowWarning(event.error) }
            } else {
                state { copy(isLoading = false, error = event.error) }
            }
        is ChatEvent.Internal.LoadingSuccess ->
            state { copy(isLoading = false, messages = event.messages, error = null) }
        is ChatEvent.Internal.GetCurrentUserIdSuccess -> {
            _currUserId = event.userId
            commands {
                +ChatCommand.GetAllMessages(
                    state.channelTitle,
                    state.channelTopicTitle,
                    currUserId
                )
            }
        }
        is ChatEvent.Ui.Reload -> {
            state { copy(isLoading = true, error = null) }
            commands {
                +ChatCommand.GetAllMessages(state.channelTitle, state.channelTopicTitle, currUserId)
            }
        }
        is ChatEvent.Ui.Search -> {
            state { copy(isLoading = true, error = null) }
            commands {
                +ChatCommand.SearchMessages(
                    channelTitle = state.channelTitle,
                    topicTitle = state.channelTopicTitle,
                    userId = currUserId,
                    query = event.query
                )
            }
        }
        is ChatEvent.Ui.SendMessage -> {
            commands {
                +ChatCommand.SendMessage(
                    channelTitle = state.channelTitle,
                    topicTitle = state.channelTopicTitle,
                    currMessages = checkMessagesLoaded(event.messages),
                    messageContent = event.messageContent
                )
            }
        }
        is ChatEvent.Ui.UpdateReaction -> {
            state { copy(isLoading = true, error = null) }
            commands {
                +ChatCommand.UpdateReaction(
                    message = event.message,
                    emoji = event.emoji,
                    checkMessagesLoaded(event.messages)
                )
            }
        }
        is ChatEvent.Ui.RemoveIrrelevantMessages -> commands {
            +ChatCommand.RemoveIrrelevantMessages(event.channelTitle, event.topicTitle)
        }
    }

    private fun checkMessagesLoaded(currList: List<Any?>): List<Any> {
        if (currList.firstOrNull { it == null } != null) {
            throw ContentHasNotLoadedException()
        }
        return currList.filterNotNull()
    }
}
