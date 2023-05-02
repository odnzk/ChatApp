package com.study.chat.presentation.chat.util.mapper


import android.content.Context
import com.study.chat.domain.model.Emoji
import com.study.chat.domain.model.IncomeMessage
import com.study.chat.presentation.chat.util.model.UiMessage
import com.study.chat.presentation.chat.util.model.UiReaction
import com.study.chat.presentation.chat.util.view.ReactionView
import com.study.chat.presentation.util.toEmojiString

internal fun IncomeMessage.mapUiReactions(currentUserId: Int): List<UiReaction> =
    reactions.groupBy { it.emoji.code }.map { group ->
        val reactions = group.value
        return@map UiReaction(
            messageId = id,
            emoji = reactions.first().emoji,
            count = reactions.size,
            isSelected = reactions.find { it.userId == currentUserId } != null,
            emojiUnicode = reactions.first().emoji.code.toEmojiString()
        )
    }

private fun UiReaction.toMessageEmojiView(
    context: Context,
    count: Int,
    isSelected: Boolean
): ReactionView = ReactionView(context).apply { setEmoji(emojiUnicode, count, isSelected) }

internal fun List<UiReaction>.toMessageEmojiViews(
    context: Context,
    message: UiMessage,
    onReactionClick: ((message: UiMessage, emoji: Emoji) -> Unit)? = null
): List<ReactionView> =
    sortedByDescending { it.count }.map { reaction ->
        reaction.toMessageEmojiView(context, reaction.count, reaction.isSelected).apply {
            setOnClickListener { onReactionClick?.invoke(message, reaction.emoji) }
        }
    }
