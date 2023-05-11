package com.study.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.study.database.entity.ChannelEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ChannelDao {

    @Query("SELECT * FROM channels WHERE is_subscribed IS :isSubscribed")
    fun getChannels(isSubscribed: Boolean): Flow<List<ChannelEntity>>

    @Query("SELECT * FROM channels WHERE title LIKE :title")
    suspend fun getChannelByTitle(title: String): ChannelEntity?

    @Upsert
    suspend fun upsertChannels(channels: List<ChannelEntity>)

    @Transaction
    suspend fun updateChannels(channels: List<ChannelEntity>, isSubscribed: Boolean) {
        deleteChannels(isSubscribed)
        upsertChannels(channels)
    }

    @Insert
    suspend fun insertChannel(channelEntity: ChannelEntity)

    @Query("DELETE FROM channels WHERE is_subscribed IS :isSubscribed")
    suspend fun deleteChannels(isSubscribed: Boolean)

    @Delete
    suspend fun delete(channelEntity: ChannelEntity)
}
