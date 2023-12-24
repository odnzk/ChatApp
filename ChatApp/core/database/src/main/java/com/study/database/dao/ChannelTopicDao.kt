package com.study.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.study.database.model.ChannelTopicEntity
import com.study.database.model.ChannelTopicEntity.Companion.TOPICS_TABLE
import kotlinx.coroutines.flow.Flow

// todo somehow move to :feature:channels
@Dao
interface ChannelTopicDao {

    @Upsert
    suspend fun upsertTopics(topics: List<ChannelTopicEntity>)

    @Query("SELECT * FROM $TOPICS_TABLE WHERE channel_id = :channelId")
    fun getTopicsByChannelId(channelId: Int): Flow<List<ChannelTopicEntity>>

    @Query("DELETE FROM $TOPICS_TABLE WHERE channel_id = :channelId")
    suspend fun deleteTopics(channelId: Int)

    @Query("DELETE FROM $TOPICS_TABLE")
    suspend fun deleteAll()

    @Transaction
    suspend fun updateTopics(topics: List<ChannelTopicEntity>, channelId: Int) {
        deleteTopics(channelId)
        upsertTopics(topics)
    }
}
