package com.study.chat.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.study.chat.data.mapper.toIncomeMessage
import com.study.chat.data.pagination.MessagesPagingMediator
import com.study.chat.domain.model.IncomeMessage
import com.study.chat.domain.model.OutcomeMessage
import com.study.chat.domain.repository.MessageRepository
import com.study.database.dataSource.MessageLocalDataSource
import com.study.database.tuple.MessageWithReactionsTuple
import com.study.network.dataSource.MessageRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RemoteMessageRepository @Inject constructor(
    private val remoteDS: MessageRemoteDataSource,
    private val localDS: MessageLocalDataSource,
    private val dispatcher: CoroutineDispatcher,
    private val messagePagingMediatorFactory: MessagesPagingMediator.Factory
) : MessageRepository {
    override fun getMessages(
        channelTitle: String,
        topicName: String,
        searchQuery: String
    ): Flow<PagingData<IncomeMessage>> =
        createMessagesPager(channelTitle, topicName, searchQuery)
            .flow
            .map { pagingData -> pagingData.map { it.toIncomeMessage() } }
            .flowOn(dispatcher)

    override suspend fun sendMessage(message: OutcomeMessage): Int = withContext(dispatcher) {
        val id = remoteDS.sendMessage(
            type = message.type,
            to = message.channelTitle,
            content = message.content,
            topic = message.topicTitle
        ).id
        requireNotNull(id)
    }

    override suspend fun addReaction(messageId: Int, emojiName: String) = withContext(dispatcher) {
        remoteDS.addReactionToMessage(messageId, emojiName)
    }

    override suspend fun removeReaction(messageId: Int, emojiName: String) =
        withContext(dispatcher) {
            remoteDS.removeReactionFromMessage(messageId, emojiName)
        }

    override suspend fun fetchMessage(messageId: Int): IncomeMessage = withContext(dispatcher) {
        requireNotNull(remoteDS.fetchSingleMessage(messageId).message).toIncomeMessage()
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun createMessagesPager(
        channelTitle: String,
        topicName: String,
        searchQuery: String
    ): Pager<Int, MessageWithReactionsTuple> =
        Pager(
            config = PagingConfig(
                PAGE_SIZE,
                enablePlaceholders = false,
                maxSize = MAX_PAGER_SIZE,
                prefetchDistance = PREFETCH_DISTANCE
            ),
            remoteMediator = messagePagingMediatorFactory.create(
                channelTitle,
                topicName,
                searchQuery
            )
        )
        { localDS.getMessages(channelTitle, topicName, searchQuery) }

    companion object {
        private const val PAGE_SIZE = 20
        private const val MAX_PAGER_SIZE = 50
        private const val PREFETCH_DISTANCE = 5
    }
}
