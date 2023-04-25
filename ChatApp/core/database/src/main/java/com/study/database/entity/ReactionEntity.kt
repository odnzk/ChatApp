package com.study.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "reactions",
    primaryKeys = ["message_id", "emoji_code", "user_id"],
    foreignKeys = [ForeignKey(
        entity = MessageEntity::class,
        parentColumns = ["id"],
        childColumns = ["message_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class ReactionEntity(
    @ColumnInfo(name = "emoji_code")
    val emojiCode: String,
    @ColumnInfo(name = "emoji_name")
    val emojiName: String,
    @ColumnInfo(name = "user_id")
    val userId: Int,
    @ColumnInfo(name = "message_id")
    val messageId: Int
)
