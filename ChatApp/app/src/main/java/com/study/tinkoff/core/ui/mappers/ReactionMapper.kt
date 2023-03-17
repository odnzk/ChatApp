package com.study.tinkoff.core.ui.mappers

import android.content.Context
import com.study.tinkoff.core.domain.model.Reaction
import com.study.tinkoff.core.ui.views.ReactionView

fun Reaction.toEmojiView(
    context: Context, count: Int
): ReactionView = ReactionView(context).apply {
    setEmoji(emojiUnicode = emoji.code, count = count)
}

fun List<Reaction>.toMessageEmojiViews(
    context: Context,
    messageId: Int,
    onReactionClick: ((messageId: Int, emojiName: String) -> Unit)? = null
): List<ReactionView> {
    return asSequence().groupBy { it.emoji.code }.map { it.value.first() to it.value.size }
        .sortedByDescending { it.second }
        .map { reactionWithCount ->
            reactionWithCount.first.toEmojiView(
                context, reactionWithCount.second
            ).apply {
                setOnClickListener {
                    isSelected = !isSelected
                    onReactionClick?.invoke(
                        messageId,
                        reactionWithCount.first.emoji.name
                    )
                }
            }
        }
}
