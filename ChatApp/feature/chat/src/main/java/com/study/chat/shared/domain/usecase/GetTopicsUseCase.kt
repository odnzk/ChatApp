package com.study.chat.shared.domain.usecase

import com.study.chat.shared.domain.repository.TopicRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class GetTopicsUseCase @Inject constructor(
    private val repository: TopicRepository,
    private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(channelId: Int): Flow<List<String>> =
        repository.getChannelTopicsTitles(channelId).flowOn(dispatcher)
}
