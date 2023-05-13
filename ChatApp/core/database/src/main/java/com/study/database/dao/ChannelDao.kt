package com.study.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.study.database.entity.ChannelEntity
import com.study.database.entity.ChannelEntity.Companion.CHANNELS_TABLE
import kotlinx.coroutines.flow.Flow


@Dao
interface ChannelDao {

    @Query("SELECT * FROM $CHANNELS_TABLE WHERE is_subscribed IS :isSubscribed")
    fun getChannels(isSubscribed: Boolean): Flow<List<ChannelEntity>>

    @Query("SELECT * FROM $CHANNELS_TABLE WHERE title LIKE :title")
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

    @Query("DELETE FROM $CHANNELS_TABLE WHERE is_subscribed IS :isSubscribed")
    suspend fun deleteChannels(isSubscribed: Boolean)

    @Delete
    suspend fun delete(channelEntity: ChannelEntity)
}
