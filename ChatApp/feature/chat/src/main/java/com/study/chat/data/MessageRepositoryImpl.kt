package com.study.chat.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.study.chat.data.mapper.toFirstMessageSenderId
import com.study.chat.data.mapper.toIncomeMessage
import com.study.chat.data.mapper.toMessageEntity
import com.study.chat.data.mapper.toReactionEntity
import com.study.chat.data.pagination.MessagesPagingMediator
import com.study.chat.domain.exceptions.MessageDoesNotExistException
import com.study.chat.domain.model.IncomeMessage
import com.study.chat.domain.model.NOT_YET_SYNCHRONIZED_ID
import com.study.chat.domain.model.OutcomeMessage
import com.study.chat.domain.model.Reaction
import com.study.chat.domain.repository.MessageRepository
import com.study.common.extension.restorePreviousStateAndThrowError
import com.study.common.extension.runCatchingNonCancellation
import com.study.database.dataSource.MessageLocalDataSource
import com.study.database.entity.tuple.MessageWithReactionsTuple
import com.study.network.dataSource.MessageRemoteDataSource
import com.study.network.model.request.message.MessageNarrowList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class MessageRepositoryImpl @Inject constructor(
    private val remoteDS: MessageRemoteDataSource,
    private val localDS: MessageLocalDataSource,
    private val dispatcher: CoroutineDispatcher,
    private val messagePagingMediatorFactory: MessagesPagingMediator.Factory
) : MessageRepository {
    override fun getMessages(
        channelId: Int,
        topicName: String?,
        searchQuery: String
    ): Flow<PagingData<IncomeMessage>> = createMessagesPager(channelId, topicName, searchQuery)
        .flow
        .map { pagingData -> pagingData.map { it.toIncomeMessage() } }
        .flowOn(dispatcher)


    override suspend fun getMessageById(messageId: Int): IncomeMessage =
        localDS.getMessageById(messageId)?.toIncomeMessage() ?: requireNotNull(
            remoteDS.getMessages(1, 0, messageId, MessageNarrowList())
                .toFirstMessageSenderId(messageId)
        )

    override suspend fun sendMessage(message: OutcomeMessage): Unit = withContext(dispatcher) {
        val entity = message.toMessageEntity()
        localDS.addMessage(entity)
        runCatchingNonCancellation {
            remoteDS.sendMessage(
                message.type,
                message.channelId,
                message.content,
                message.topicTitle
            )
        }.onSuccess { response ->
            localDS.updateMessageId(NOT_YET_SYNCHRONIZED_ID, requireNotNull(response.id))
        }.onFailure {
            localDS.deleteMessage(NOT_YET_SYNCHRONIZED_ID)
        }
    }

    override suspend fun updateMessage(messageId: Int, content: String, topic: String) {
        val entity = localDS.updateMessage(messageId, content, topic)
            ?: throw MessageDoesNotExistException()
        restorePreviousStateAndThrowError({ remoteDS.updateMessage(messageId, content, topic) }) {
            localDS.updateMessage(entity)
        }
    }

    override suspend fun deleteIrrelevantMessages(channelId: Int, topicTitle: String) {
        localDS.removeIrrelevantMessages(channelId, topicTitle)
    }

    override suspend fun deleteMessage(messageId: Int) {
        val entity = localDS.deleteMessage(messageId) ?: throw MessageDoesNotExistException()
        restorePreviousStateAndThrowError(action = { remoteDS.deleteMessage(messageId) }) {
            localDS.addMessage(entity)
        }
    }

    override suspend fun addReaction(reaction: Reaction): Unit = withContext(dispatcher) {
        val entity = reaction.toReactionEntity()
        localDS.addReaction(entity)
        restorePreviousStateAndThrowError(
            action = { remoteDS.addReaction(reaction.messageId, reaction.emoji.name) }) {
            localDS.removeReaction(entity)
        }
    }

    override suspend fun removeReaction(reaction: Reaction): Unit = withContext(dispatcher) {
        val entity = reaction.toReactionEntity()
        localDS.removeReaction(entity)
        restorePreviousStateAndThrowError(
            action = { remoteDS.removeReaction(reaction.messageId, reaction.emoji.name) }) {
            localDS.addReaction(entity)
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun createMessagesPager(
        channelId: Int, topicName: String?, searchQuery: String
    ): Pager<Int, MessageWithReactionsTuple> = Pager(
        config = PagingConfig(
            PAGE_SIZE, enablePlaceholders = false,
            maxSize = MAX_PAGER_SIZE,
            prefetchDistance = PREFETCH_DISTANCE,
            initialLoadSize = PAGE_SIZE * 2
        ),
        remoteMediator = messagePagingMediatorFactory.create(channelId, topicName, searchQuery)
    )
    { localDS.getMessages(channelId, topicName, searchQuery) }

    companion object {
        private const val PAGE_SIZE = 20
        private const val MAX_PAGER_SIZE = 50
        private const val PREFETCH_DISTANCE = 5
    }

}
