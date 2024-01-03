package com.study.chat.actions.domain.repository

import com.study.chat.actions.domain.model.User
import com.study.chat.common.domain.model.IncomeMessage

internal interface ActionsRepository {
    suspend fun copyMessage(messageId: Int)
    suspend fun deleteMessage(messageId: Int)
    suspend fun getMessageById(messageId: Int): IncomeMessage
    suspend fun getCurrentUser(): User
}
