package com.study.chat.domain.repository

import androidx.paging.PagingData
import com.study.chat.domain.model.IncomeMessage
import com.study.chat.domain.model.OutcomeMessage
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun getMessages(
        channelTitle: String,
        topicName: String,
        searchQuery: String
    ): Flow<PagingData<IncomeMessage>>

    suspend fun sendMessage(message: OutcomeMessage): Int
    suspend fun addReaction(messageId: Int, emojiName: String)
    suspend fun removeReaction(messageId: Int, emojiName: String)
    suspend fun removeIrrelevant(channelTitle: String, topicTitle: String)
}
