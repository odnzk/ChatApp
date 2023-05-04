package com.study.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.study.database.entity.MessageEntity
import com.study.database.entity.tuple.MessageWithReactionsTuple

@Dao
interface MessageDao {
    @Transaction
    @Query(
        "SELECT * FROM messages" +
                " WHERE channel_title LIKE :channelTitle" +
                " AND topic_title LIKE :topicTitle" +
                " AND LOWER(channel_title) LIKE '%' || LOWER(:query) || '%'" +
                " ORDER BY calendar DESC"
    )
    fun getMessages(
        channelTitle: String,
        topicTitle: String,
        query: String
    ): PagingSource<Int, MessageWithReactionsTuple>

    @Query(
        "SELECT COUNT(*) FROM messages" +
                " WHERE channel_title LIKE :channelTitle" +
                " AND topic_title LIKE :topicTitle"
    )
    suspend fun countMessagesByChannelAndTopic(
        channelTitle: String,
        topicTitle: String
    ): Int

    @Query("SELECT COUNT(*) FROM messages")
    suspend fun countAllMessages(): Int

    @Query("SELECT * FROM messages")
    suspend fun getAll(): List<MessageEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(messages: List<MessageEntity>)

    @Upsert
    suspend fun upsert(messages: List<MessageEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(message: MessageEntity)

    @Transaction
    @Delete
    suspend fun delete(messages: List<MessageEntity>)

    @Transaction
    @Query("DELETE FROM messages")
    suspend fun deleteAll()

    @Transaction
    @Query(
        "DELETE FROM messages" +
                " WHERE channel_title = :channelTitle" +
                " AND topic_title = :topicTitle" +
                " AND id IN (SELECT id FROM messages ORDER BY calendar LIMIT :count)"
    )
    suspend fun deleteIrrelevant(channelTitle: String, topicTitle: String, count: Int)

    @Query(
        "SELECT * FROM messages " +
                "WHERE id = :id"
    )
    suspend fun getMessageById(id: Int): MessageEntity?

}
