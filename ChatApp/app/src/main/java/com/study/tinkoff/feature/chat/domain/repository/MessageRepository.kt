package com.study.tinkoff.feature.chat.domain.repository

import com.study.domain.model.IncomeMessage
import com.study.domain.model.OutcomeMessage

interface MessageRepository {
    suspend fun getMessages(): List<IncomeMessage>
    suspend fun sendMessage(message: OutcomeMessage)
    suspend fun addReaction(messageId: Int, emojiName: String)
}
