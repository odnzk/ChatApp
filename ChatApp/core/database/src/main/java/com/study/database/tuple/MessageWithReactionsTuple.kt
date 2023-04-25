package com.study.database.tuple

import androidx.room.Embedded
import androidx.room.Relation
import com.study.database.entity.MessageEntity
import com.study.database.entity.ReactionEntity

data class MessageWithReactionsTuple(
    @Embedded val message: MessageEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "message_id"
    ) val reactions: List<ReactionEntity>
)
