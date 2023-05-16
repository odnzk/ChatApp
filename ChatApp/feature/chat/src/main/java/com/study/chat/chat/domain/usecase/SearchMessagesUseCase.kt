package com.study.chat.chat.domain.usecase

import androidx.paging.PagingData
import com.study.chat.chat.domain.repository.ChatRepository
import com.study.chat.shared.domain.model.IncomeMessage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class SearchMessagesUseCase @Inject constructor(
    private val repository: ChatRepository,
    private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(
        channelId: Int,
        channelTopicTitle: String?,
        query: String
    ): Flow<PagingData<IncomeMessage>> =
        repository.getMessages(channelId, channelTopicTitle, query).flowOn(dispatcher)
}
