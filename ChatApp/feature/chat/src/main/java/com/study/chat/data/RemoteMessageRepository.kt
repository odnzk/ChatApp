package com.study.chat.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.study.chat.data.mapper.toIncomeMessage
import com.study.chat.data.pagination.MessagesPagingSource
import com.study.chat.domain.model.IncomeMessage
import com.study.chat.domain.model.OutcomeMessage
import com.study.chat.domain.repository.MessageRepository
import com.study.network.repository.MessageDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RemoteMessageRepository @Inject constructor(
    private val dataSource: MessageDataSource,
    private val dispatcher: CoroutineDispatcher,
    private val messagePaginSourceFactory: MessagesPagingSource.Factory
) :
    MessageRepository {
    override fun getMessages(
        channelTitle: String,
        topicName: String,
        searchQuery: String
    ): Flow<PagingData<IncomeMessage>> =
        createLatestMessagesPager(channelTitle, topicName, searchQuery).flow.flowOn(dispatcher)

    override suspend fun sendMessage(message: OutcomeMessage): Int = withContext(dispatcher) {
        val id = dataSource.sendMessage(
            type = message.type,
            to = message.channelTitle,
            content = message.content,
            topic = message.topicTitle
        ).id
        requireNotNull(id)
    }

    override suspend fun addReaction(messageId: Int, emojiName: String) = withContext(dispatcher) {
        dataSource.addReactionToMessage(messageId, emojiName)
    }

    override suspend fun removeReaction(messageId: Int, emojiName: String) =
        withContext(dispatcher) {
            dataSource.removeReactionFromMessage(messageId, emojiName)
        }

    override suspend fun fetchMessage(messageId: Int): IncomeMessage = withContext(dispatcher) {
        requireNotNull(dataSource.fetchSingleMessage(messageId).message).toIncomeMessage()
    }

    private fun createLatestMessagesPager(
        channelTitle: String,
        topicName: String,
        searchQuery: String
    ): Pager<Int, IncomeMessage> =
        Pager(
            PagingConfig(
                PAGE_SIZE,
                enablePlaceholders = false,
                maxSize = MAX_PAGER_SIZE,
                prefetchDistance = 2
            )
        )
        { messagePaginSourceFactory.create(channelTitle, topicName, searchQuery) }

    companion object {
        private const val PAGE_SIZE = 10
        private const val MAX_PAGER_SIZE = 40
    }
}
