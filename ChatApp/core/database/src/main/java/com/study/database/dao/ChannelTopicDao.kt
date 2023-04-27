package com.study.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.study.database.entity.ChannelTopicEntity

@Dao
interface ChannelTopicDao {

    @Query("SELECT * FROM topics WHERE channel_id = :channelId")
    suspend fun getTopicsByChannelTitle(channelId: Int): List<ChannelTopicEntity>

    @Upsert
    suspend fun upsertTopics(topics: List<ChannelTopicEntity>)
}
