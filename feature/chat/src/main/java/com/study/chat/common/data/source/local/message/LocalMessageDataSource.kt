package com.study.chat.common.data.source.local.message

import androidx.paging.PagingSource
import com.study.chat.common.data.source.local.message.dao.MessageDao
import com.study.chat.common.data.source.local.message.dao.ReactionDao
import com.study.chat.common.data.source.local.message.entity.MessageEntity
import com.study.chat.common.data.source.local.message.entity.ReactionEntity
import com.study.chat.common.data.source.local.message.entity.MessageWithReactionsTuple
import dagger.Reusable
import javax.inject.Inject

@Reusable
internal class LocalMessageDataSource @Inject constructor(
    private val messageDao: MessageDao,
    private val reactionDao: ReactionDao
) {

    suspend fun removeIrrelevantMessages(channelId: Int, topicTitle: String) =
        (messageDao.countMessagesByChannelAndTopic(channelId, topicTitle) - MAX_MESSAGES_COUNT)
            .takeIf { diff -> diff > 0 }
            ?.let { messageDao.deleteIrrelevant(channelId, topicTitle, it) }

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

    suspend fun addMessage(messageEntity: MessageEntity) = messageDao.insert(listOf(messageEntity))

    suspend fun updateMessageId(prevId: Int, newId: Int) = messageDao.updateMessageId(prevId, newId)

    suspend fun updateMessage(messageEntity: MessageEntity) = messageDao.update(messageEntity)

    suspend fun addReaction(reactionEntity: ReactionEntity) = reactionDao.insert(reactionEntity)

    suspend fun removeReaction(reactionEntity: ReactionEntity) = reactionDao.delete(reactionEntity)

    suspend fun deleteMessage(messageId: Int): MessageEntity? =
        getMessageById(messageId)?.also { deletedMessage ->
            messageDao.delete(listOf(deletedMessage))
        }

    suspend fun getMessageById(messageId: Int): MessageEntity? =
        messageDao.getMessageById(messageId)

    suspend fun updateMessage(messageId: Int, content: String, topic: String): MessageEntity? =
        messageDao.getMessageById(messageId)?.also { entity ->
            messageDao.update(entity.copy(content = content, topicTitle = topic))
        }

    companion object {
        private const val MAX_MESSAGES_COUNT = 50
    }
}
