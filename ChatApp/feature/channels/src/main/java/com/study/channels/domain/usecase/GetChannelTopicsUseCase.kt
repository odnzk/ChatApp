package com.study.channels.domain.usecase

import com.study.channels.domain.model.ChannelTopic
import com.study.channels.domain.repository.ChannelRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class GetChannelTopicsUseCase @Inject constructor(
    private val repository: ChannelRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        channelId: Int
    ): List<ChannelTopic> = withContext(dispatcher) {
        repository.getChannelTopics(channelId)
    }
}
