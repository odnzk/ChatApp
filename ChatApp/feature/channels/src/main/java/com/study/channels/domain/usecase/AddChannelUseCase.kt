package com.study.channels.domain.usecase

import com.study.channels.domain.repository.ChannelRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class AddChannelUseCase @Inject constructor(
    private val repository: ChannelRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(title: String, isHistoryPublic: Boolean) =
        withContext(dispatcher) { repository.addChannel(title, isHistoryPublic) }

}
