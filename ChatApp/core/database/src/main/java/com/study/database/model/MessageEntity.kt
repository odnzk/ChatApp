package com.study.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.study.database.model.MessageEntity.Companion.MESSAGES_TABLE
import java.util.Calendar

// todo somehow move to :feature:channels
@Entity(
    tableName = MESSAGES_TABLE,
    indices = [Index(value = ["topic_title", "channel_id"])]
)
data class MessageEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Int,
    @ColumnInfo("channel_id")
    val channelId: Int,
    @ColumnInfo("topic_title")
    val topicTitle: String,
    @ColumnInfo(name = "sender_id")
    val senderId: Int,
    @ColumnInfo(name = "sender_avatar")
    val senderAvatarUrl: String?,
    @ColumnInfo(name = "sender_name")
    val senderName: String?,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "calendar")
    val calendar: Calendar
) {
    companion object {
        const val MESSAGES_TABLE = "messages"
    }
}
