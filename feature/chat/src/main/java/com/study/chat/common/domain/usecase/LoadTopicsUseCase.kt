package com.study.chat.common.domain.usecase

import com.study.chat.common.domain.repository.TopicRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class LoadTopicsUseCase @Inject constructor(
    private val repository: TopicRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(channelId: Int) = withContext(dispatcher) {
        repository.loadChannelTopics(channelId)
    }
}
