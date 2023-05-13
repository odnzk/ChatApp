package com.study.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.study.database.entity.ChannelTopicEntity
import com.study.database.entity.ChannelTopicEntity.Companion.TOPICS_TABLE
import com.study.database.entity.MessageEntity.Companion.MESSAGES_TABLE
import com.study.database.entity.tuple.TopicWithMessagesTuple
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelTopicDao {

    @Query("SELECT * FROM $TOPICS_TABLE WHERE channel_id = :channelId")
    fun getTopicsByChannelTitle(channelId: Int): Flow<List<ChannelTopicEntity>>

    @Transaction
    suspend fun updateTopics(topics: List<ChannelTopicEntity>, channelId: Int) {
        deleteTopics(channelId)
        upsertTopics(topics)
    }


    @Upsert
    suspend fun upsertTopics(topics: List<ChannelTopicEntity>)

    @Query("DELETE FROM $TOPICS_TABLE WHERE channel_id = :channelId")
    suspend fun deleteTopics(channelId: Int)

    @Query(
        "SELECT t.*, m.message_count" +
                " FROM $TOPICS_TABLE AS t" +
                " LEFT JOIN (" +
                "    SELECT topic_title, COUNT(*) AS message_count" +
                "    FROM $MESSAGES_TABLE" +
                "    GROUP BY topic_title" +
                " ) AS m" +
                " ON t.title LIKE m.topic_title" +
                " WHERE t.channel_id LIKE :channelId"
    )
    fun getTopicsWithMessagesCount(channelId: Int): Flow<List<TopicWithMessagesTuple>>
}
