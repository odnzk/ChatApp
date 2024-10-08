package com.study.channels.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.study.channels.data.source.local.entity.ChannelTopicEntity
import com.study.channels.data.source.local.entity.ChannelTopicEntity.Companion.TOPICS_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelTopicDao {

    @Upsert
    suspend fun upsertTopics(topics: List<ChannelTopicEntity>)

    @Query("SELECT * FROM $TOPICS_TABLE WHERE channel_id = :channelId")
    fun getTopicsByChannelId(channelId: Int): Flow<List<ChannelTopicEntity>>

    @Query("DELETE FROM $TOPICS_TABLE WHERE channel_id = :channelId")
    suspend fun deleteTopics(channelId: Int)

    @Transaction
    suspend fun updateTopics(topics: List<ChannelTopicEntity>, channelId: Int) {
        deleteTopics(channelId)
        upsertTopics(topics)
    }
}
