package com.study.tinkoff.feature.chat.domain.repository

import com.study.tinkoff.core.domain.model.message.IncomeMessage
import com.study.tinkoff.core.domain.model.message.OutcomeMessage

interface MessageRepository {
    fun getMessages(): List<IncomeMessage>
    suspend fun sendMessage(message: OutcomeMessage)
    suspend fun addReaction(messageId: Int, emojiName: String)
}
