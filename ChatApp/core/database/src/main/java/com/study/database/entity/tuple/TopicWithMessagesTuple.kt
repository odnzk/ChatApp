package com.study.database.entity.tuple

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.study.database.entity.ChannelTopicEntity

data class TopicWithMessagesTuple(
    @Embedded val topic: ChannelTopicEntity,
    @ColumnInfo("message_count")
    val messageCount: Int
)
