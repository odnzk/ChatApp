package com.study.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.study.database.entity.ChannelEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ChannelDao {

    @Upsert
    suspend fun upsertChannels(channels: List<ChannelEntity>)

    @Query("SELECT * FROM channels WHERE is_subscribed IS :isSubscribed")
    fun getChannels(isSubscribed: Boolean): Flow<List<ChannelEntity>>
}
