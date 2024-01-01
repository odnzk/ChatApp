package com.study.chat.chat.presentation.elm

import androidx.paging.cachedIn
import com.study.auth.api.UserAuthRepository
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
import com.study.chat.common.domain.model.NOT_YET_SYNCHRONIZED_ID
import com.study.chat.common.domain.model.SynchronizationException
import com.study.chat.common.domain.usecase.GetTopicsUseCase
import com.study.chat.common.domain.usecase.LoadTopicsUseCase
import com.study.common.ext.toFlow
import com.study.common.search.SearchListener
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import vivid.money.elmslie.core.switcher.Switcher
import vivid.money.elmslie.coroutines.Actor
import vivid.money.elmslie.coroutines.switch

internal class ChatActor @AssistedInject constructor(
    @Assisted("channelId") private val channelId: Int,
    @Assisted("topicTitle") private val topicTitle: String?,
    private val userAuthRepository: UserAuthRepository,
    private val getAllMessageUseCase: GetAllMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val addReactionUseCase: AddReactionUseCase,
    private val removeReactionUseCase: RemoveReactionUseCase,
    private val searchMessagesUseCase: SearchMessagesUseCase,
    private val removeIrrelevantMessages: RemoveIrrelevantMessagesUseCase,
    private val loadTopicsUseCase: LoadTopicsUseCase,
    private val getTopicsUseCase: GetTopicsUseCase,
    private val uploadFileUseCase: UploadFileUseCase,
    private val chatScope: CoroutineScope
) : Actor<ChatCommand, Internal>, SearchListener {

    private val topicsSwitcher = Switcher()
    private val messagesSwitcher = Switcher()
    override fun execute(command: ChatCommand): Flow<Internal> = when (command) {
        is ChatCommand.GetAllMessages -> messagesSwitcher.switch {
            getAllMessageUseCase(
                channelId,
                topicTitle
            )
                .cachedIn(chatScope)
                .distinctUntilChanged()
                .map {
                    it.toUiMessagesWithSeparators(
                        userAuthRepository.getUserId(),
                        topicTitle == null
                    )
                }
                .mapEvents(Internal::LoadingSuccess, Internal::LoadingError)
        }

        is ChatCommand.SendMessage -> toFlow {
            sendMessageUseCase(
                channelId = channelId,
                content = command.messageContent,
                topicTitle = command.topic
            )
        }.mapEvents(errorMapper = Internal::LoadingError)

        is ChatCommand.UpdateReaction -> toFlow {
            if (command.message.id == NOT_YET_SYNCHRONIZED_ID) throw SynchronizationException()

            val message = command.message
            val reaction = command.emoji.toReaction(message.id, userAuthRepository.getUserId())
            message.reactions
                .find { it.emoji.code == command.emoji.code }
                ?.takeIf { it.isSelected }
                ?.let { removeReactionUseCase(reaction) } ?: addReactionUseCase(reaction)
        }.mapEvents(
            eventMapper = { Internal.UpdateReactionSuccess },
            errorMapper = Internal::LoadingError
        )

        is ChatCommand.RemoveIrrelevantMessages ->
            toFlow { removeIrrelevantMessages(channelId, command.topic) }
                .mapEvents(errorMapper = Internal::LoadingError)

        is ChatCommand.LoadTopics ->
            toFlow { loadTopicsUseCase(channelId) }
                .mapEvents(errorMapper = Internal::LoadingError)

        is ChatCommand.GetTopics -> topicsSwitcher.switch {
            getTopicsUseCase(channelId)
                .mapEvents(
                    eventMapper = Internal::GettingTopicsSuccess,
                    errorMapper = Internal::LoadingError
                )
        }

        is ChatCommand.UploadFile -> toFlow {
            uploadFileUseCase(
                channelId = channelId,
                topicTitle = command.topic,
                uri = command.uri
            )
        }.mapEvents(
            eventMapper = { Internal.FileUploaded },
            errorMapper = Internal::UploadingFileError
        )
    }

    override fun onNewQuery(query: String) {
        searchMessagesUseCase(
            channelTopicTitle = topicTitle,
            channelId = channelId,
            query = query
        )
            .map {
                it.toUiMessagesWithSeparators(
                    userAuthRepository.getUserId(),
                    topicTitle == null
                )
            }
            .distinctUntilChanged()
            .mapEvents(Internal::LoadingSuccess, Internal::LoadingError)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("channelId") channelId: Int,
            @Assisted("topicTitle") topicTitle: String?
        ): ChatActor
    }
}
