package com.study.channels.domain.usecase

import com.study.channels.domain.model.Channel
import com.study.channels.domain.repository.ChannelRepository
import com.study.channels.presentation.util.model.ChannelFilter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetChannelsUseCase @Inject constructor(
    private val repository: ChannelRepository,
    private val dispatcher: CoroutineDispatcher
) {
    internal suspend operator fun invoke(
        filter: ChannelFilter
    ): List<Channel> = withContext(dispatcher) {
        when (filter) {
            ChannelFilter.ALL -> repository.getAllChannels()
            ChannelFilter.SUBSCRIBED_ONLY -> repository.getSubscribedChannels()
        }
    }
}
