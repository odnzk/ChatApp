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
import com.study.database.tuple.MessageWithReactionsTuple

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
    suspend fun clearAndInsert(messages: List<MessageEntity>) {
        deleteAll()
        insert(messages)
    }

    @Transaction
    @Query("DELETE FROM messages")
    suspend fun deleteAll()

    @Transaction
    @Query("DELETE FROM messages WHERE channel_title = :channelTitle AND topic_title = :topicTitle")
    suspend fun deleteByChannelAndTopic(channelTitle: String, topicTitle: String)
}
