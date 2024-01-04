package com.study.chat.chat.domain.usecase

import com.study.chat.chat.domain.repository.ChatRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RemoveIrrelevantMessagesUseCase @Inject constructor(
    private val repository: ChatRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(channelId: Int, topicTitle: String) =
        withContext(dispatcher) { repository.deleteIrrelevantMessages(channelId, topicTitle) }
}
