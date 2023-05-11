package com.study.channels.domain.usecase

import com.study.channels.domain.model.Channel
import com.study.channels.domain.repository.ChannelRepository
import com.study.common.search.Searcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class SearchChannelUseCase @Inject constructor(
    private val channelSearcher: Searcher<Channel>,
    private val repository: ChannelRepository,
    private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(query: String, isSubscribed: Boolean): Flow<List<Channel>> =
        channelSearcher.toSearchFlow(query, repository.getChannels(isSubscribed)).flowOn(dispatcher)
}

