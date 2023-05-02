package com.study.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.study.database.entity.ChannelTopicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelTopicDao {

    @Query("SELECT * FROM topics WHERE channel_id = :channelId")
    fun getTopicsByChannelTitle(channelId: Int): Flow<List<ChannelTopicEntity>>

    @Upsert
    suspend fun upsertTopics(topics: List<ChannelTopicEntity>)
}
