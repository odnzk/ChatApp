package com.study.chat.presentation.chat.elm

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.study.auth.api.UserNotAuthorizedException
import com.study.chat.domain.exceptions.SynchronizationException
import com.study.chat.domain.model.Emoji
import com.study.chat.domain.usecase.GetAllMessagesUseCase
import com.study.chat.domain.usecase.GetCurrentUserIdUseCase
import com.study.chat.domain.usecase.RemoveIrrelevantMessagesUseCase
import com.study.chat.domain.usecase.SearchMessagesUseCase
import com.study.chat.domain.usecase.SendMessageUseCase
import com.study.chat.domain.usecase.UpdateReactionUseCase
import com.study.chat.presentation.chat.elm.ChatEvent.Internal
import com.study.chat.presentation.chat.util.mapper.groupByDate
import com.study.chat.presentation.chat.util.mapper.toUiMessages
import com.study.chat.presentation.chat.util.model.UiMessage
import com.study.chat.presentation.chat.util.model.UiReaction
import com.study.chat.presentation.util.toEmojiString
import com.study.common.extension.findWithIndex
import com.study.common.extension.isSameDay
import com.study.common.extension.toFlow
import com.study.common.extension.update
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import vivid.money.elmslie.coroutines.Actor
import javax.inject.Inject

internal class ChatActor @Inject constructor(
    private val getAllMessageUseCase: GetAllMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val updateReactionUseCase: UpdateReactionUseCase,
    private val searchMessagesUseCase: SearchMessagesUseCase,
    private val getCurrentUserId: GetCurrentUserIdUseCase,
    private val removeIrrelevantMessages: RemoveIrrelevantMessagesUseCase,
    private val chatScope: CoroutineScope,
    private val dispatcher: CoroutineDispatcher
) : Actor<ChatCommand, Internal> {

    override fun execute(command: ChatCommand): Flow<Internal> = when (command) {
        is ChatCommand.GetAllMessages -> getAllMessageUseCase(
            command.channelTitle,
            command.topicTitle
        )
            .cachedIn(chatScope)
            .map { it.toUiMessages(command.userId).groupByDate() }
            .mapEvents(Internal::LoadingSuccess, Internal::LoadingError)

        is ChatCommand.SearchMessages -> searchMessagesUseCase(
            channelTopicTitle = command.topicTitle,
            channelTitle = command.channelTitle,
            query = command.query
        )
            .map { it.toUiMessages(command.userId).groupByDate() }
            .mapEvents(Internal::LoadingSuccess, Internal::LoadingError)

        is ChatCommand.SendMessage -> flow {
            val newMessage = UiMessage.MeMessage(command.messageContent)
            emit(addMessageLocally(newMessage, command.currMessages))
            coroutineScope {
                newMessage.id = sendMessageUseCase(
                    command.channelTitle, command.messageContent, command.topicTitle
                )
            }
        }
            .flowOn(dispatcher)
            .mapEvents(::mapToLoadingSuccess, Internal::LoadingError)

        is ChatCommand.UpdateReaction -> flow {
            if (command.message.id == UiMessage.NOT_YET_SYNCHRONIZED_ID) throw SynchronizationException()
            val message = command.message
            val messageIndex = command.currMessages.indexOf(message)
            val emoji = command.emoji

            val (reaction, index) = message.reactions.findWithIndex { it.emoji.code == emoji.code }
            val updatedMessage = updateReactionsLocally(message, emoji, reaction, index)
            emit(command.currMessages.toMutableList().update(messageIndex, updatedMessage))

            coroutineScope {
                val prevSelected = message.reactions.find { it.isSelected }
                updateReactionUseCase(
                    reaction?.isSelected == true, message.id, emoji.name, prevSelected?.emoji?.name
                )
            }
        }.mapEvents(::mapToLoadingSuccess, Internal::LoadingError)
        ChatCommand.GetCurrentUserId -> {
            toFlow { getCurrentUserId() }
                .mapEvents(
                    eventMapper = Internal::GetCurrentUserIdSuccess,
                    errorMapper = { Internal.LoadingError(UserNotAuthorizedException()) })
        }
        is ChatCommand.RemoveIrrelevantMessages ->
            toFlow(dispatcher + Job()) {
                removeIrrelevantMessages(command.channelTitle, command.topicTitle)
            }.mapEvents(errorMapper = Internal::LoadingError)
    }

    private fun addMessageLocally(message: UiMessage.MeMessage, list: List<Any>): List<Any> {
        val lastMessageDate = list.filterIsInstance<UiMessage>().last().calendar
        return list.toMutableList().apply {
            if (lastMessageDate.isSameDay(message.calendar)) {
                add(0, message)
            } else {
                add(0, message.calendar)
                add(0, message)
            }
        }
    }

    private fun updateReactionsLocally(
        message: UiMessage, emoji: Emoji, reaction: UiReaction?, reactionIndex: Int?
    ): UiMessage {
        val updatedList = message.reactions.toMutableList().apply {
            if (reaction?.isSelected == true && reactionIndex != null) {
                updateOrRemove(reaction, reactionIndex)
            } else {
                val (prevSelected, prevIndex) = findWithIndex { it.isSelected }
                if (prevSelected != null && prevIndex != null) {
                    updateOrRemove(prevSelected, prevIndex)
                }
                if (reaction != null && reactionIndex != null) {
                    update(
                        reactionIndex,
                        reaction.copy(isSelected = true, count = reaction.count + 1)
                    )
                } else {
                    add(
                        UiReaction(
                            messageId = message.id,
                            emojiUnicode = emoji.code.toEmojiString(),
                            emoji = emoji,
                            count = 1,
                            isSelected = true
                        )
                    )
                }
            }
        }
        return message.baseCopy(reactions = updatedList)
    }

    private fun MutableList<UiReaction>.updateOrRemove(reaction: UiReaction, reactionIndex: Int) {
        if (reaction.count > 1) {
            update(reactionIndex, reaction.copy(isSelected = false, count = reaction.count - 1))
        } else {
            remove(reaction)
        }
    }

    private fun <T : Any> mapToLoadingSuccess(list: List<T>) =
        Internal.LoadingSuccess(PagingData.from(list))
}
