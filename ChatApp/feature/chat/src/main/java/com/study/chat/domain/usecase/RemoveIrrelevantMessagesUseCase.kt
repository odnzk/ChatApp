package com.study.chat.domain.usecase

import com.study.chat.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoveIrrelevantMessagesUseCase @Inject constructor(
    private val repository: MessageRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(channelId: Int, topicTitle: String) =
        withContext(dispatcher) { repository.removeIrrelevant(channelId, topicTitle) }
}
