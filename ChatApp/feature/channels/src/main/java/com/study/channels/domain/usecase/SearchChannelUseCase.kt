package com.study.channels.domain.usecase

import com.study.channels.domain.model.Channel
import com.study.channels.domain.repository.ChannelRepository
import com.study.channels.presentation.util.model.ChannelFilter
import com.study.components.search.searchNotEmptyQuery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class SearchChannelUseCase @Inject constructor(
    private val repository: ChannelRepository, private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(query: String, filter: ChannelFilter): List<Channel> =
        withContext(dispatcher) {
            when (filter) {
                ChannelFilter.ALL -> repository.getAllChannels()
                ChannelFilter.SUBSCRIBED_ONLY -> repository.getSubscribedChannels()
            }.searchNotEmptyQuery(query) { it.title.contains(query, ignoreCase = true) }
        }
}

