package com.study.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.study.database.model.ChannelTopicEntity.Companion.TOPICS_TABLE

@Entity(
    tableName = TOPICS_TABLE,
    indices = [Index(value = ["channel_id"])],
    foreignKeys = [ForeignKey(
        entity = ChannelEntity::class,
        parentColumns = ["id"],
        childColumns = ["channel_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
class ChannelTopicEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "channel_id")
    val channelId: Int,
    @ColumnInfo(name = "title")
    val title: String
) {
    companion object {
        const val TOPICS_TABLE = "topics"
    }
}

