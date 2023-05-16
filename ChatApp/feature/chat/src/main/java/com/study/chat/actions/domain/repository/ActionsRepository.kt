package com.study.chat.actions.domain.repository

import com.study.chat.shared.domain.model.IncomeMessage

internal interface ActionsRepository {
    suspend fun copyMessage(messageId: Int)
    suspend fun deleteMessage(messageId: Int)
    suspend fun getMessageById(messageId: Int): IncomeMessage
}
