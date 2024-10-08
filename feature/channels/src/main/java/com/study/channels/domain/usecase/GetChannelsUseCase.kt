package com.study.channels.domain.usecase

import com.study.channels.domain.repository.ChannelRepository
import com.study.channels.domain.model.Channel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class GetChannelsUseCase @Inject constructor(
    private val repository: ChannelRepository,
    private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(isSubscribed: Boolean): Flow<List<Channel>> =
        repository.getChannels(isSubscribed, "").flowOn(dispatcher)
}
