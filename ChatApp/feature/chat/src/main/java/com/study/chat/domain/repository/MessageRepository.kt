package com.study.chat.domain.repository

import androidx.paging.PagingData
import com.study.chat.domain.model.IncomeMessage
import com.study.chat.domain.model.OutcomeMessage
import com.study.chat.domain.model.Reaction
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun getMessages(
        channelId: Int,
        topicName: String?,
        searchQuery: String
    ): Flow<PagingData<IncomeMessage>>

    suspend fun sendMessage(message: OutcomeMessage)
    suspend fun addReaction(reaction: Reaction)
    suspend fun removeReaction(reaction: Reaction)
    suspend fun removeIrrelevant(channelId: Int, topicTitle: String)
}
