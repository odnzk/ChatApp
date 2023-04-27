package com.study.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(
    tableName = "messages",
    indices = [Index(value = ["topic_title", "channel_title"], unique = true)]
)
class MessageEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Int,
    @ColumnInfo("channel_title")
    val channelTitle: String,
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
)
