package com.study.chat.domain.usecase

import androidx.paging.PagingData
import com.study.chat.domain.model.IncomeMessage
import com.study.chat.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class SearchMessagesUseCase(
    private val repository: MessageRepository,
    private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(
        channelTitle: String,
        channelTopicTitle: String,
        query: String
    ): Flow<PagingData<IncomeMessage>> =
        repository.getMessages(channelTitle, channelTopicTitle, query).flowOn(dispatcher)
}
