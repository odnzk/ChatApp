package com.study.chat.common.data.mapper

import com.study.chat.common.domain.model.Emoji
import com.study.chat.common.domain.model.Reaction
import com.study.chat.common.data.source.local.message.entity.ReactionEntity
import com.study.network.model.response.message.ReactionDto

internal fun Reaction.toReactionEntity() = ReactionEntity(
    emojiCode = emoji.code, emojiName = emoji.name, userId = userId, messageId = messageId
)

internal fun List<ReactionEntity>.toReactions() = map { it.toReaction() }

internal fun List<ReactionDto?>?.toReactionEntities(messageId: Int): List<ReactionEntity> =
    this?.filterNotNull()?.map { it.toReactionEntity(messageId) } ?: emptyList()

private fun ReactionDto.toReactionEntity(messageId: Int): ReactionEntity = ReactionEntity(
    messageId = messageId,
    userId = userId,
    emojiName = emojiName,
    emojiCode = emojiCode
)

private fun ReactionEntity.toReaction(): Reaction =
    Reaction(messageId = messageId, userId = userId, emoji = Emoji(emojiName, emojiCode))

internal fun ReactionDto.toReaction(messageId: Int) =
    Reaction(
        messageId = messageId, userId = userId, emoji =
        Emoji(emojiCode, emojiName)
    )
