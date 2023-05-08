package com.study.chat.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.study.chat.data.mapper.toIncomeMessage
import com.study.chat.data.mapper.toMessageEntity
import com.study.chat.data.mapper.toReactionEntity
import com.study.chat.data.pagination.MessagesPagingMediator
import com.study.chat.domain.model.IncomeMessage
import com.study.chat.domain.model.OutcomeMessage
import com.study.chat.domain.model.Reaction
import com.study.chat.domain.repository.MessageRepository
import com.study.database.dataSource.MessageLocalDataSource
import com.study.database.entity.tuple.MessageWithReactionsTuple
import com.study.network.dataSource.MessageRemoteDataSource
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

    override suspend fun sendMessage(message: OutcomeMessage) = withContext(dispatcher) {
        val messageEntity = message.toMessageEntity()
        localDS.addMessage(messageEntity)
        val id = remoteDS.sendMessage(
            message.type,
            message.channelId,
            message.content,
            message.topicTitle
        ).id
        localDS.updateMessage(messageEntity.copy(id = requireNotNull(id)))
    }

    override suspend fun addReaction(reaction: Reaction) =
        withContext(dispatcher) {
            localDS.addReaction(reaction.toReactionEntity())
            remoteDS.addReaction(reaction.messageId, reaction.emoji.name)
        }

    override suspend fun removeReaction(reaction: Reaction) = withContext(dispatcher) {
        localDS.removeReaction(reaction.toReactionEntity())
        remoteDS.removeReaction(reaction.messageId, reaction.emoji.name)
    }

    override suspend fun removeIrrelevant(channelId: Int, topicTitle: String) {
        localDS.removeIrrelevantMessages(channelId, topicTitle)
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun createMessagesPager(
        channelId: Int,
        topicName: String?,
        searchQuery: String
    ): Pager<Int, MessageWithReactionsTuple> = Pager(
        config = PagingConfig(
            PAGE_SIZE,
            enablePlaceholders = false,
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
