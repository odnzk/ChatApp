package com.study.channels.domain.usecase

import com.study.channels.domain.model.Channel
import com.study.channels.domain.model.ChannelFilter
import com.study.channels.domain.repository.ChannelRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetChannelsUseCase(
    private val repository: ChannelRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        filter: ChannelFilter
    ): List<Channel> = withContext(dispatcher) {
        when (filter) {
            ChannelFilter.ALL -> repository.getAllChannels()
            ChannelFilter.SUBSCRIBED_ONLY -> repository.getSubscribedChannels()
        }
    }
}
