package com.study.chat.chat.domain.repository

import androidx.paging.PagingData
import com.study.chat.common.domain.model.IncomeMessage
import com.study.chat.common.domain.model.OutcomeMessage
import com.study.chat.common.domain.model.Reaction
import kotlinx.coroutines.flow.Flow

internal interface ChatRepository {
    fun getMessages(
        channelId: Int,
        topicName: String?,
        searchQuery: String
    ): Flow<PagingData<IncomeMessage>>

    suspend fun sendMessage(message: OutcomeMessage)
    suspend fun deleteIrrelevantMessages(channelId: Int, topicTitle: String)
    suspend fun addReaction(reaction: Reaction)
    suspend fun removeReaction(reaction: Reaction)
}
