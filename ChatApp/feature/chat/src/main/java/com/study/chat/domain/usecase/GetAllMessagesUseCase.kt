package com.study.chat.domain.usecase

import androidx.paging.PagingData
import com.study.chat.domain.model.IncomeMessage
import com.study.chat.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAllMessagesUseCase @Inject constructor(
    private val repository: MessageRepository,
    private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(
        channelTitle: String,
        topicTitle: String
    ): Flow<PagingData<IncomeMessage>> = repository
        .getMessages(channelTitle = channelTitle, topicName = topicTitle, searchQuery = "")
        .flowOn(dispatcher)
}
