package com.study.chat.domain.usecase

import com.study.chat.domain.repository.ChannelRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class GetTopicsUseCase @Inject constructor(
    private val repository: ChannelRepository,
    private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(channelId: Int): Flow<List<String>> =
        repository.getChannelTopicsTitles(channelId).flowOn(dispatcher)
}
