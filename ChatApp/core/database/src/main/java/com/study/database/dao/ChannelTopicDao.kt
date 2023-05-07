package com.study.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.study.database.entity.ChannelTopicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelTopicDao {

    @Query("SELECT * FROM topics WHERE channel_id = :channelId")
    fun getTopicsByChannelTitle(channelId: Int): Flow<List<ChannelTopicEntity>>

    @Transaction
    suspend fun updateTopics(topics: List<ChannelTopicEntity>, channelId: Int) {
        deleteTopics(channelId)
        upsertTopics(topics)
    }


    @Upsert
    suspend fun upsertTopics(topics: List<ChannelTopicEntity>)

    @Query("DELETE FROM topics WHERE channel_id = :channelId")
    suspend fun deleteTopics(channelId: Int)
}
