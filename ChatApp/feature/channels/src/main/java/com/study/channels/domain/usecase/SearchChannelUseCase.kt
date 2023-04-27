package com.study.channels.domain.usecase

import com.study.channels.domain.model.Channel
import com.study.channels.domain.model.ChannelFilter
import com.study.channels.domain.repository.ChannelRepository
import com.study.components.search.searchFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class SearchChannelUseCase @Inject constructor(
    private val repository: ChannelRepository, private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(query: String, filter: ChannelFilter): Flow<List<Channel>> =
        repository.getChannels(filter)
            .searchFlow(query) { it.title.contains(query, ignoreCase = true) }
            .flowOn(dispatcher)
}

