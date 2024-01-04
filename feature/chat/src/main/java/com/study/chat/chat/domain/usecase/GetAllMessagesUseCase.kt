package com.study.chat.chat.domain.usecase

import androidx.paging.PagingData
import com.study.chat.chat.domain.repository.ChatRepository
import com.study.chat.common.domain.model.IncomeMessage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class GetAllMessagesUseCase @Inject constructor(
    private val repository: ChatRepository,
    private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(
        channelId: Int,
        topicTitle: String?
    ): Flow<PagingData<IncomeMessage>> = repository
        .getMessages(channelId, topicName = topicTitle, searchQuery = "")
        .flowOn(dispatcher)
}
