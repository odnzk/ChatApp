package com.study.chat.domain.repository

import androidx.paging.PagingData
import com.study.chat.domain.model.IncomeMessage
import com.study.chat.domain.model.OutcomeMessage
import com.study.chat.domain.model.Reaction
import kotlinx.coroutines.flow.Flow

internal interface MessageRepository {
    fun getMessages(
        channelId: Int,
        topicName: String?,
        searchQuery: String
    ): Flow<PagingData<IncomeMessage>>

    suspend fun getMessageById(messageId: Int): IncomeMessage
    suspend fun sendMessage(message: OutcomeMessage)

    suspend fun deleteMessage(messageId: Int)
    suspend fun updateMessage(messageId: Int, content: String, topic: String)
    suspend fun deleteIrrelevantMessages(channelId: Int, topicTitle: String)
    suspend fun addReaction(reaction: Reaction)
    suspend fun removeReaction(reaction: Reaction)
}
