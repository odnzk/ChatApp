package com.study.chat.presentation.chat.elm

import androidx.paging.cachedIn
import com.study.auth.api.UserNotAuthorizedException
import com.study.chat.domain.exceptions.SynchronizationException
import com.study.chat.domain.model.NOT_YET_SYNCHRONIZED_ID
import com.study.chat.domain.usecase.AddReactionUseCase
import com.study.chat.domain.usecase.GetAllMessagesUseCase
import com.study.chat.domain.usecase.GetCurrentUserIdUseCase
import com.study.chat.domain.usecase.RemoveIrrelevantMessagesUseCase
import com.study.chat.domain.usecase.RemoveReactionUseCase
import com.study.chat.domain.usecase.SearchMessagesUseCase
import com.study.chat.domain.usecase.SendMessageUseCase
import com.study.chat.presentation.chat.elm.ChatEvent.Internal
import com.study.chat.presentation.chat.util.mapper.toReaction
import com.study.chat.presentation.chat.util.mapper.toUiMessagesWithSeparators
import com.study.common.extension.toFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import vivid.money.elmslie.coroutines.Actor
import javax.inject.Inject

internal class ChatActor @Inject constructor(
    private val getAllMessageUseCase: GetAllMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val addReactionUseCase: AddReactionUseCase,
    private val removeReactionUseCase: RemoveReactionUseCase,
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
            .map { it.toUiMessagesWithSeparators(command.userId, command.topicTitle == null) }
            .mapEvents(Internal::LoadingSuccess, Internal::LoadingError)

        is ChatCommand.SearchMessages -> searchMessagesUseCase(
            channelTopicTitle = command.topicTitle,
            channelTitle = command.channelTitle,
            query = command.query
        )
            .map { it.toUiMessagesWithSeparators(command.userId, command.topicTitle == null) }
            .mapEvents(Internal::LoadingSuccess, Internal::LoadingError)

        is ChatCommand.SendMessage -> toFlow {
            sendMessageUseCase(
                command.channelTitle,
                command.messageContent,
                command.topicTitle,
                command.senderId
            )
        }.flowOn(dispatcher)
            .mapEvents(errorMapper = Internal::LoadingError)
        is ChatCommand.UpdateReaction -> flow<Unit> {
            if (command.message.id == NOT_YET_SYNCHRONIZED_ID) throw SynchronizationException()
            val message = command.message
            val reaction = command.emoji.toReaction(message.id, command.userId)
            message.reactions.find { it.emoji.code == command.emoji.code }?.takeIf { it.isSelected }
                ?.let {
                    removeReactionUseCase(reaction)
                } ?: addReactionUseCase(reaction)
        }.mapEvents(
            eventMapper = { Internal.UpdateReactionSuccess },
            errorMapper = Internal::LoadingError
        )
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
}
