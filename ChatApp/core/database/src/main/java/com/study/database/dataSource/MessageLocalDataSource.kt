package com.study.database.dataSource

import androidx.paging.PagingSource
import com.study.database.dao.MessageDao
import com.study.database.dao.ReactionDao
import com.study.database.entity.MessageEntity
import com.study.database.entity.ReactionEntity
import com.study.database.entity.tuple.MessageWithReactionsTuple
import dagger.Reusable
import javax.inject.Inject

@Reusable
class MessageLocalDataSource @Inject constructor(
    private val messageDao: MessageDao,
    private val reactionDao: ReactionDao
) {
    suspend fun removeIrrelevantMessages(
        channelTitle: String,
        topicTitle: String
    ) {
        val diff =
            messageDao.countMessagesByChannelAndTopic(channelTitle, topicTitle) - MAX_MESSAGES_COUNT
        if (diff > 0) {
            messageDao.deleteIrrelevant(channelTitle, topicTitle, diff)
        }
    }

    suspend fun addMessagesWithReactions(
        messages: List<MessageEntity>,
        reactions: List<ReactionEntity>
    ) {
        messageDao.upsert(messages)
        reactionDao.upsert(reactions)
    }

    fun getMessages(
        channelTitle: String,
        topicTitle: String,
        query: String
    ): PagingSource<Int, MessageWithReactionsTuple> {
        return messageDao.getMessages(channelTitle, topicTitle, query)
    }

    companion object {
        private const val MAX_MESSAGES_COUNT = 50
    }

}
