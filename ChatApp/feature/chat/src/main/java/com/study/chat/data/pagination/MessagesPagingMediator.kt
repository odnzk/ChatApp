package com.study.chat.data.pagination

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.study.chat.data.mapper.getAllReactionEntities
import com.study.chat.data.mapper.toMessageEntities
import com.study.database.dataSource.MessageLocalDataSource
import com.study.database.tuple.MessageWithReactionsTuple
import com.study.network.dataSource.MessageRemoteDataSource
import com.study.network.model.request.message.MessageNarrow
import com.study.network.model.request.message.MessageNarrowList
import com.study.network.model.request.message.MessageNarrowOperator
import com.study.network.model.request.message.MessagesAnchor
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MessagesPagingMediator @AssistedInject constructor(
    private val localDs: MessageLocalDataSource,
    private val remoteDS: MessageRemoteDataSource,
    @Assisted("query") query: String,
    @Assisted("channelTitle") private val channelTitle: String,
    @Assisted("topicName") private val topicTitle: String,
) : RemoteMediator<Int, MessageWithReactionsTuple>() {

    private val topicNarrow = MessageNarrow(MessageNarrowOperator.TOPIC, topicTitle)
    private val channelNarrow = MessageNarrow(MessageNarrowOperator.STREAM, channelTitle)
    private val searchNarrow = MessageNarrow(MessageNarrowOperator.SEARCH, query)
    private val narrowList = MessageNarrowList(mutableListOf(topicNarrow, channelNarrow))

    init {
        if (query.isNotEmpty()) narrowList.add(searchNarrow)
    }

    override suspend fun initialize(): InitializeAction = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, MessageWithReactionsTuple>
    ): MediatorResult {
        val lastItemId: Int = when (loadType) {
            LoadType.REFRESH -> state.lastItemOrNull()?.message?.id ?: DEFAULT_LAST_MESSAGE_ID
            LoadType.PREPEND -> state.lastItemOrNull()?.message?.id
                ?: return MediatorResult.Success(true)
            LoadType.APPEND -> state.lastItemOrNull()?.message?.id ?: return MediatorResult.Success(
                true
            )
        }
        return try {
            val pageSize = state.config.pageSize
            val response = if (lastItemId == DEFAULT_LAST_MESSAGE_ID) {
                remoteDS.getMessages(
                    anchor = MessagesAnchor.NEWEST,
                    numBefore = pageSize,
                    numAfter = 0,
                    narrow = narrowList
                )
            } else {
                remoteDS.getMessages(
                    anchorMessageId = lastItemId,
                    numBefore = pageSize,
                    numAfter = 0,
                    narrow = narrowList
                )
            }
            val messages = response.toMessageEntities(channelTitle, topicTitle)
            val reactions = response.getAllReactionEntities()
            if (loadType == LoadType.REFRESH) {
                localDs.clearAndInsertMessages(messages)
                localDs.clearAndInsertReactions(reactions)
            } else {
                localDs.upsertMessages(messages)
                localDs.upsertReactions(reactions)
            }

            MediatorResult.Success(messages.isEmpty())
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("channelTitle") channelTitle: String,
            @Assisted("topicName") topicName: String,
            @Assisted("query") query: String
        ): MessagesPagingMediator
    }

    companion object {
        private const val DEFAULT_LAST_MESSAGE_ID = -1
    }
}
