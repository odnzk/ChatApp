package com.study.channels.domain.usecase

import com.study.channels.domain.model.ChannelTopic
import com.study.channels.domain.repository.ChannelRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class GetChannelTopicsUseCase @Inject constructor(
    private val repository: ChannelRepository,
    private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(
        channelId: Int
    ): Flow<List<ChannelTopic>> = repository.getChannelTopics(channelId).flowOn(dispatcher)
}
