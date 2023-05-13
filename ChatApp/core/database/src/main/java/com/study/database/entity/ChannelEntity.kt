package com.study.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.study.database.entity.ChannelEntity.Companion.CHANNELS_TABLE

@Entity(
    tableName = CHANNELS_TABLE,
    indices = [Index(value = ["title"])],
)
class ChannelEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "is_subscribed")
    val isSubscribed: Boolean,
    @ColumnInfo(name = "color")
    val color: String?
) {
    companion object {
        const val CHANNELS_TABLE = "channels"
    }
}
