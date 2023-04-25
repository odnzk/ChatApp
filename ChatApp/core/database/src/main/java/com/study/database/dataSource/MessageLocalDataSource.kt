package com.study.database.dataSource

import androidx.paging.PagingSource
import com.study.database.dao.MessageDao
import com.study.database.dao.ReactionDao
import com.study.database.entity.MessageEntity
import com.study.database.entity.ReactionEntity
import com.study.database.tuple.MessageWithReactionsTuple
import dagger.Reusable
import javax.inject.Inject

@Reusable
class MessageLocalDataSource @Inject constructor(
    private val messageDao: MessageDao,
    private val reactionDao: ReactionDao
) {
    suspend fun clearAndInsertMessages(messages: List<MessageEntity>) {
        messageDao.clearAndInsert(messages)
    }

    suspend fun clearAndInsertReactions(reactions: List<ReactionEntity>) {
        reactionDao.clearAndInsert(reactions)
    }

    suspend fun upsertMessages(messages: List<MessageEntity>) {
        messageDao.upsert(messages)
    }

    suspend fun upsertReactions(reactions: List<ReactionEntity>) {
        reactionDao.upsert(reactions)
    }

    fun getMessages(
        channelTitle: String,
        topicTitle: String,
        query: String
    ): PagingSource<Int, MessageWithReactionsTuple> =
        messageDao.getMessages(channelTitle, topicTitle, query)


}
