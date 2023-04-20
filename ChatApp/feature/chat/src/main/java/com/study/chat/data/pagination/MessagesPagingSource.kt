package com.study.chat.data.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.study.chat.data.mapper.toMessageList
import com.study.chat.domain.model.IncomeMessage
import com.study.network.model.request.message.MessageNarrow
import com.study.network.model.request.message.MessageNarrowList
import com.study.network.model.request.message.MessageNarrowOperator
import com.study.network.model.request.message.MessagesAnchor
import com.study.network.repository.MessageDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException

internal class MessagesPagingSource @AssistedInject constructor(
    private val dataSource: MessageDataSource,
    @Assisted("channelTitle") channelTitle: String,
    @Assisted("topicName") topicName: String,
    @Assisted("searchQuery") searchQuery: String
) : PagingSource<Int, IncomeMessage>() {
    private var anchorPosition = DEFAULT_LAST_MESSAGE_ID
    private val topicNarrow = MessageNarrow(
        MessageNarrowOperator.TOPIC, topicName
    )
    private val channelNarrow = MessageNarrow(
        MessageNarrowOperator.STREAM, channelTitle
    )
    private val narrowList = if (searchQuery.isNotBlank()) {
        MessageNarrowList(
            topicNarrow, channelNarrow, MessageNarrow(
                MessageNarrowOperator.SEARCH, searchQuery
            )
        )
    } else MessageNarrowList(topicNarrow, channelNarrow)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, IncomeMessage> {
        return try {
            val pageNumber = params.key ?: INITIAL_PAGE_NUMBER
            val pageSize = params.loadSize

            val response = if (anchorPosition == DEFAULT_LAST_MESSAGE_ID) {
                dataSource.getMessages(
                    anchor = MessagesAnchor.NEWEST,
                    numBefore = pageSize,
                    numAfter = 0,
                    narrow = narrowList
                )
            } else {
                dataSource.getMessages(
                    anchorMessageId = anchorPosition,
                    numBefore = pageSize,
                    numAfter = 0,
                    narrow = narrowList
                )
            }

            val messageList = response.toMessageList()
            anchorPosition = messageList.last().id

            val nextPageNumber = if (messageList.size < pageSize) null else pageNumber + 1
            val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null

            LoadResult.Page(messageList, prevPageNumber, nextPageNumber)
        } catch (ex: HttpException) {
            LoadResult.Error(ex)
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, IncomeMessage>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    companion object {
        private const val DEFAULT_LAST_MESSAGE_ID = -1
        const val INITIAL_PAGE_NUMBER = 1
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("channelTitle") channelTitle: String,
            @Assisted("topicName") topicName: String,
            @Assisted("searchQuery") searchQuery: String
        ): MessagesPagingSource
    }
}
