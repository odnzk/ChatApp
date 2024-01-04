package com.study.chat.common.domain.usecase

import com.study.chat.common.domain.repository.TopicRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class GetTopicsUseCase @Inject constructor(
    private val repository: TopicRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(channelId: Int): List<String> = withContext(dispatcher) {
        repository.getChannelTopicsTitles(channelId)
    }

}
