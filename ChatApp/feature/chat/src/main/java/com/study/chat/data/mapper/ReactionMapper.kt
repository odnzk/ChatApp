package com.study.chat.data.mapper

import com.study.chat.domain.model.Reaction
import com.study.database.entity.ReactionEntity

internal fun Reaction.toReactionEntity() = ReactionEntity(
    emojiCode = emoji.code, emojiName = emoji.name, userId = userId, messageId = messageId
)
