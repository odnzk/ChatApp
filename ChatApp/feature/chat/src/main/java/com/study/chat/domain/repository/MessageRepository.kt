package com.study.chat.domain.repository

import com.study.chat.domain.model.IncomeMessage
import com.study.chat.domain.model.OutcomeMessage
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun getMessages(): Flow<List<IncomeMessage>>
    suspend fun sendMessage(message: OutcomeMessage)
    suspend fun addReaction(messageId: Int, emojiName: String)
}
