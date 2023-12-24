package com.study.chat.edit.domain.repository

import com.study.chat.common.domain.model.IncomeMessage

internal interface EditMessageRepository {
    suspend fun getMessageById(messageId: Int): IncomeMessage
    suspend fun updateMessage(messageId: Int, content: String, topic: String)
}
