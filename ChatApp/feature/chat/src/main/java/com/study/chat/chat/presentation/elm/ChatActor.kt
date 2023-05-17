package com.study.chat.chat.presentation.elm

import androidx.paging.cachedIn
import com.study.auth.api.UserNotAuthorizedException
import com.study.chat.chat.domain.usecase.AddReactionUseCase
import com.study.chat.chat.domain.usecase.GetAllMessagesUseCase
import com.study.chat.chat.domain.usecase.RemoveIrrelevantMessagesUseCase
import com.study.chat.chat.domain.usecase.RemoveReactionUseCase
import com.study.chat.chat.domain.usecase.SearchMessagesUseCase
import com.study.chat.chat.domain.usecase.SendMessageUseCase
import com.study.chat.chat.domain.usecase.UploadFileUseCase
import com.study.chat.chat.presentation.elm.ChatEvent.Internal
import com.study.chat.chat.presentation.util.mapper.toReaction
import com.study.chat.chat.presentation.util.mapper.toUiMessagesWithSeparators
import com.study.chat.shared.domain.model.NOT_YET_SYNCHRONIZED_ID
import com.study.chat.shared.domain.model.SynchronizationException
import com.study.chat.shared.domain.usecase.GetCurrentUserIdUseCase
import com.study.chat.shared.domain.usecase.GetTopicsUseCase
import com.study.chat.shared.domain.usecase.LoadTopicsUseCase
import com.study.common.extension.toFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import vivid.money.elmslie.core.switcher.Switcher
import vivid.money.elmslie.coroutines.Actor
import vivid.money.elmslie.coroutines.switch
import javax.inject.Inject

internal class ChatActor @Inject constructor(
    private val getAllMessageUseCase: GetAllMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val addReactionUseCase: AddReactionUseCase,
    private val removeReactionUseCase: RemoveReactionUseCase,
    private val searchMessagesUseCase: SearchMessagesUseCase,
    private val getCurrentUserId: GetCurrentUserIdUseCase,
    private val removeIrrelevantMessages: RemoveIrrelevantMessagesUseCase,
    private val loadTopicsUseCase: LoadTopicsUseCase,
    private val getTopicsUseCase: GetTopicsUseCase,
    private val uploadFileUseCase: UploadFileUseCase,
    private val chatScope: CoroutineScope
) : Actor<ChatCommand, Internal> {

    private val topicsSwitcher = Switcher()
    private val messagesSwitcher = Switcher()
    override fun execute(command: ChatCommand): Flow<Internal> = when (command) {
        is ChatCommand.GetAllMessages -> messagesSwitcher.switch {
            getAllMessageUseCase(
                command.channelId,
                command.topicTitle
            )
                .cachedIn(chatScope)
                .distinctUntilChanged()
                .map { it.toUiMessagesWithSeparators(command.userId, command.topicTitle == null) }
                .mapEvents(Internal::LoadingSuccess, Internal::LoadingError)
        }
        is ChatCommand.SearchMessages -> searchMessagesUseCase(
            channelTopicTitle = command.topicTitle,
            channelId = command.channelId,
            query = command.query
        )
            .map { it.toUiMessagesWithSeparators(command.userId, command.topicTitle == null) }
            .distinctUntilChanged()
            .mapEvents(Internal::LoadingSuccess, Internal::LoadingError)
        is ChatCommand.SendMessage -> toFlow {
            sendMessageUseCase(
                command.channelId,
                command.messageContent,
                command.topicTitle,
                command.senderId
            )
        }.mapEvents(errorMapper = Internal::LoadingError)
        is ChatCommand.UpdateReaction -> toFlow {
            if (command.message.id == NOT_YET_SYNCHRONIZED_ID) throw SynchronizationException()

            val message = command.message
            val reaction = command.emoji.toReaction(message.id, command.userId)
            message.reactions
                .find { it.emoji.code == command.emoji.code }
                ?.takeIf { it.isSelected }
                ?.let { removeReactionUseCase(reaction) } ?: addReactionUseCase(reaction)
        }.mapEvents(
            eventMapper = { Internal.UpdateReactionSuccess },
            errorMapper = Internal::LoadingError
        )
        ChatCommand.GetCurrentUserId -> toFlow { getCurrentUserId() }
            .mapEvents(
                eventMapper = Internal::GetCurrentUserIdSuccess,
                errorMapper = { Internal.LoadingError(UserNotAuthorizedException()) }
            )
        is ChatCommand.RemoveIrrelevantMessages ->
            toFlow { removeIrrelevantMessages(command.channelId, command.topicTitle) }
                .mapEvents(errorMapper = Internal::LoadingError)
        is ChatCommand.LoadTopics ->
            toFlow { loadTopicsUseCase(command.channelId) }
                .mapEvents(errorMapper = Internal::LoadingError)
        is ChatCommand.GetTopics -> topicsSwitcher.switch {
            getTopicsUseCase(command.channelId)
                .mapEvents(
                    eventMapper = Internal::GettingTopicsSuccess,
                    errorMapper = Internal::LoadingError
                )
        }
        is ChatCommand.UploadFile -> toFlow {
            uploadFileUseCase(
                channelId = command.channelId,
                topicTitle = command.topicTitle,
                senderId = command.senderId,
                uri = command.uri
            )
        }.mapEvents(
            eventMapper = { Internal.FileUploaded },
            errorMapper = Internal::UploadingFileError
        )
    }
}
