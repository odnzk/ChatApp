package com.study.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.study.database.model.ChannelEntity
import com.study.database.model.ChannelEntity.Companion.CHANNELS_TABLE
import com.study.database.model.update.ChannelUpdateEntity
import com.study.database.util.mapper.toChannelUpdateEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface ChannelDao {
    @Insert
    suspend fun insertChannel(channelEntity: ChannelEntity)

    @Upsert
    suspend fun upsertChannels(channels: List<ChannelEntity>)

    @Upsert(entity = ChannelEntity::class)
    suspend fun upsertWithoutIsSubscribed(channels: List<ChannelUpdateEntity>)

    @Delete
    suspend fun delete(channelEntity: ChannelEntity)

    @Query(
        "SELECT * FROM $CHANNELS_TABLE" +
                " WHERE is_subscribed IS :isSubscribed AND 1" +
                " AND LOWER(title) LIKE '%' || LOWER(:query) || '%'"
    )
    fun getChannels(isSubscribed: Boolean, query: String): Flow<List<ChannelEntity>>

    @Query("SELECT * FROM $CHANNELS_TABLE WHERE title LIKE :title")
    suspend fun getChannelByTitle(title: String): ChannelEntity?

    @Query("DELETE FROM $CHANNELS_TABLE WHERE is_subscribed IS :isSubscribed")
    suspend fun deleteChannels(isSubscribed: Boolean)

    @Transaction
    suspend fun updateChannels(channels: List<ChannelEntity>, isSubscribed: Boolean) {
        deleteChannels(isSubscribed)
        if (isSubscribed) {
            upsertChannels(channels)
        } else {
            upsertWithoutIsSubscribed(channels.toChannelUpdateEntity())
        }
    }
}
