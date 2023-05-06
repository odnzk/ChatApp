package com.study.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.study.database.entity.ChannelEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ChannelDao {

    @Upsert
    suspend fun upsertChannels(channels: List<ChannelEntity>)

    @Transaction
    suspend fun updateChannels(channels: List<ChannelEntity>, isSubscribed: Boolean) {
        deleteChannels(isSubscribed)
        upsertChannels(channels)
    }

    @Insert
    suspend fun insertChannel(channelEntity: ChannelEntity)

    @Query("SELECT * FROM channels WHERE is_subscribed IS :isSubscribed")
    fun getChannels(isSubscribed: Boolean): Flow<List<ChannelEntity>>

    @Query("DELETE FROM channels WHERE is_subscribed IS :isSubscribed")
    suspend fun deleteChannels(isSubscribed: Boolean)
}
