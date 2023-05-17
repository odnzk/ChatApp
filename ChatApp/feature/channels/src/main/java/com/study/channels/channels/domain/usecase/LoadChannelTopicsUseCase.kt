package com.study.channels.channels.domain.usecase

import com.study.channels.channels.domain.repository.ChannelRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class LoadChannelTopicsUseCase @Inject constructor(
    private val repository: ChannelRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(channelId: Int) = withContext(dispatcher) {
        repository.loadChannelTopics(channelId)
    }
}
