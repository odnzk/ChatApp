package com.study.chat.domain.usecase

import com.study.chat.domain.repository.ChannelRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadTopicsUseCase @Inject constructor(
    private val repository: ChannelRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(channelId: Int) = withContext(dispatcher) {
        repository.loadChannelTopics(channelId)
    }
}
