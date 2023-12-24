package com.study.channels.channels.domain.usecase

import com.study.channels.channels.domain.repository.ChannelRepository
import com.study.channels.common.domain.model.Channel
import com.study.common.search.toSearchFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class SearchChannelUseCase @Inject constructor(
    private val repository: ChannelRepository,
    private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(query: String, isSubscribed: Boolean): Flow<List<Channel>> =
        repository.getChannels(isSubscribed, query).toSearchFlow().flowOn(dispatcher)
}

