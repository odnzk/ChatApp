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
        channelId: Int,
        topicTitle: String
    ) = (messageDao.countMessagesByChannelAndTopic(channelId, topicTitle) - MAX_MESSAGES_COUNT)
        .takeIf { it > 0 }?.let { diff -> messageDao.deleteIrrelevant(channelId, topicTitle, diff) }


    suspend fun addMessagesWithReactions(
        messages: List<MessageEntity>,
        reactions: List<ReactionEntity>
    ) {
        messageDao.upsert(messages)
        reactionDao.upsert(reactions)
    }

    fun getMessages(
        channelId: Int,
        topicTitle: String?,
        query: String
    ): PagingSource<Int, MessageWithReactionsTuple> =
        topicTitle?.let { messageDao.getMessages(channelId, topicTitle, query) }
            ?: messageDao.getMessages(channelId, query)

    suspend fun addMessage(messageEntity: MessageEntity) {
        messageDao.insert(messageEntity)
    }

    suspend fun updateMessage(messageEntity: MessageEntity) {
        messageDao.update(messageEntity)
    }

    suspend fun addReaction(reactionEntity: ReactionEntity) {
        reactionDao.insert(reactionEntity)
    }

    suspend fun removeReaction(reactionEntity: ReactionEntity) {
        reactionDao.delete(reactionEntity)
    }

    companion object {
        private const val MAX_MESSAGES_COUNT = 50
    }

}
