package com.study.channels.domain.usecase

import com.study.channels.domain.model.ChannelFilter
import com.study.channels.domain.repository.ChannelRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class UpdateChannelsUseCase @Inject constructor(
    private val repository: ChannelRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        filter: ChannelFilter
    ): Unit = withContext(dispatcher) { repository.loadChannels(filter) }

}
