package com.study.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.study.database.entity.MessageEntity
import com.study.database.entity.MessageEntity.Companion.MESSAGES_TABLE
import com.study.database.entity.tuple.MessageWithReactionsTuple

@Dao
interface MessageDao {
    @Transaction
    @Query(
        "SELECT * FROM $MESSAGES_TABLE" +
                " WHERE channel_id = :channelId" +
                " AND topic_title LIKE :topicTitle" +
                " AND LOWER(content) LIKE '%' || LOWER(:query) || '%'" +
                " ORDER BY calendar DESC"
    )
    fun getMessages(
        channelId: Int,
        topicTitle: String,
        query: String
    ): PagingSource<Int, MessageWithReactionsTuple>

    @Transaction
    @Query(
        "SELECT * FROM $MESSAGES_TABLE" +
                " WHERE channel_id = :channelId" +
                " AND LOWER(content) LIKE '%' || LOWER(:query) || '%'" +
                " ORDER BY calendar DESC"
    )
    fun getMessages(
        channelId: Int,
        query: String
    ): PagingSource<Int, MessageWithReactionsTuple>

    @Query("SELECT * FROM $MESSAGES_TABLE WHERE id = :id")
    suspend fun getMessageById(id: Int): MessageEntity?

    @Query(
        "SELECT COUNT(*) FROM $MESSAGES_TABLE" +
                " WHERE channel_id = :channelId" +
                " AND topic_title LIKE :topicTitle"
    )
    suspend fun countMessagesByChannelAndTopic(
        channelId: Int,
        topicTitle: String
    ): Int

    @Query("SELECT COUNT(*) FROM $MESSAGES_TABLE")
    suspend fun countAllMessages(): Int


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(messages: List<MessageEntity>)

    @Upsert
    suspend fun upsert(messages: List<MessageEntity>)

    @Update
    suspend fun update(message: MessageEntity)

    @Query("UPDATE $MESSAGES_TABLE SET id = :newId WHERE id = :prevId")
    suspend fun updateMessageId(prevId: Int, newId: Int)

    @Transaction
    @Delete
    suspend fun delete(messages: List<MessageEntity>)

    @Transaction
    @Query("DELETE FROM $MESSAGES_TABLE")
    suspend fun deleteAll()

    @Transaction
    @Query(
        "DELETE FROM $MESSAGES_TABLE" +
                " WHERE channel_id = :channelId" +
                " AND topic_title = :topicTitle" +
                " AND id IN (SELECT id FROM $MESSAGES_TABLE ORDER BY calendar LIMIT :count)"
    )
    suspend fun deleteIrrelevant(channelId: Int, topicTitle: String, count: Int)


}
