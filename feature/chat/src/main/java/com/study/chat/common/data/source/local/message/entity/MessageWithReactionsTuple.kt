package com.study.chat.common.data.source.local.message.entity

import androidx.room.Embedded
import androidx.room.Relation

data class MessageWithReactionsTuple(
    @Embedded val message: MessageEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "message_id"
    ) val reactions: List<ReactionEntity>
)
