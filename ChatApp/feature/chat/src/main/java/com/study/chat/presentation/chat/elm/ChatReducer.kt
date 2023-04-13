package com.study.chat.presentation.chat.elm

import com.study.chat.domain.exceptions.ContentHasNotLoadedException
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer


internal class ChatReducer : DslReducer<ChatEvent, ChatState, ChatEffect, ChatCommand>() {

    private var _currUserId: Int? = null
    private val currUserId get() = checkNotNull(_currUserId) { "Current user id cannot be null" }

    override fun Result.reduce(event: ChatEvent) = when (event) {
        is ChatEvent.Internal.LoadingError -> {
            state { copy(isLoading = false) }
            effects { +ChatEffect.ShowError(event.error) }
        }
        is ChatEvent.Internal.LoadingSuccess ->
            state { copy(isLoading = false, messages = event.messages) }
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
        is ChatEvent.Ui.Init -> {
            state { copy(isLoading = true) }
            commands { +ChatCommand.GetCurrentUserId }
        }
        is ChatEvent.Ui.Reload -> {
            state { copy(isLoading = true) }
            commands {
                +ChatCommand.GetAllMessages(state.channelTitle, state.channelTopicTitle, currUserId)
            }
        }
        is ChatEvent.Ui.Search -> {
            state { copy(isLoading = true) }
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
            state { copy(isLoading = true) }
            commands {
                +ChatCommand.UpdateReaction(
                    message = event.message,
                    emoji = event.emoji,
                    checkMessagesLoaded(event.messages)
                )
            }
        }
    }

    private fun checkMessagesLoaded(currList: List<Any?>): List<Any> {
        if (currList.firstOrNull { it == null } != null) {
            throw ContentHasNotLoadedException()
        }
        return currList.filterNotNull()
    }
}
