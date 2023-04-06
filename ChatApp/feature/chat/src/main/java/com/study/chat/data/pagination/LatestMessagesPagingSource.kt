package com.study.chat.data.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.study.chat.data.mapper.toMessageList
import com.study.chat.domain.model.IncomeMessage
import com.study.network.NetworkModule
import com.study.network.model.request.message.MessageNarrow
import com.study.network.model.request.message.MessageNarrowList
import com.study.network.model.request.message.MessageNarrowOperator
import com.study.network.model.request.message.MessagesAnchor
import retrofit2.HttpException

internal class LatestMessagesPagingSource(
    private val channelTitle: String,
    private val topicName: String
) : PagingSource<Int, IncomeMessage>() {
    private val api = NetworkModule.providesApi()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, IncomeMessage> {
        return try {
            val pageNumber = params.key ?: INITIAL_PAGE_NUMBER
            val pageSize = params.loadSize

            val response = api.getMessages(
                anchor = MessagesAnchor.NEWEST,
                numBefore = pageSize,
                numAfter = 0,
                narrow = MessageNarrowList(
                    MessageNarrow(
                        operator = MessageNarrowOperator.TOPIC,
                        operand = topicName
                    ),
                    MessageNarrow(
                        operator = MessageNarrowOperator.STREAM,
                        operand = channelTitle
                    )
                )
            )

            val messageList = response.toMessageList()
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
        const val INITIAL_PAGE_NUMBER = 1
    }
}
