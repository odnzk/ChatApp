package com.study.database.model.tuple

import androidx.room.Embedded
import androidx.room.Relation
import com.study.database.model.MessageEntity
import com.study.database.model.ReactionEntity

// todo somehow move to :feature:channels
// data class??
data class MessageWithReactionsTuple(
    @Embedded val message: MessageEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "message_id"
    ) val reactions: List<ReactionEntity>
)
