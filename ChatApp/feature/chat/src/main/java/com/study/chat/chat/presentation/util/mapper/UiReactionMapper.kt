package com.study.chat.chat.presentation.util.mapper


import android.content.Context
import com.study.chat.chat.presentation.model.UiEmoji
import com.study.chat.chat.presentation.model.UiMessage
import com.study.chat.chat.presentation.model.UiReaction
import com.study.chat.chat.presentation.util.view.ReactionView
import com.study.chat.shared.domain.model.IncomeMessage
import com.study.chat.shared.domain.model.Reaction
import com.study.chat.shared.presentation.util.toEmojiString

internal fun IncomeMessage.mapUiReactions(currentUserId: Int): List<UiReaction> =
    reactions.groupBy { it.emoji.code }.map { group ->
        val reactions = group.value
        return@map UiReaction(
            messageId = id,
            emoji = reactions.first().emoji.toUiEmoji(),
            count = reactions.size,
            isSelected = reactions.find { it.userId == currentUserId } != null,
            emojiUnicode = reactions.first().emoji.code.toEmojiString()
        )
    }

internal fun List<UiReaction>.toMessageEmojiViews(
    context: Context,
    message: UiMessage,
    onReactionClick: ((message: UiMessage, emoji: UiEmoji) -> Unit)? = null
): List<ReactionView> =
    sortedByDescending { it.count }.sortedByDescending { it.isSelected }.map { reaction ->
        reaction.toMessageEmojiView(context, reaction.count, reaction.isSelected).apply {
            setOnClickListener { onReactionClick?.invoke(message, reaction.emoji) }
        }
    }

internal fun UiEmoji.toReaction(messageId: Int, userId: Int): Reaction =
    Reaction(messageId = messageId, userId = userId, emoji = toEmoji())

private fun UiReaction.toMessageEmojiView(
    context: Context,
    count: Int,
    isSelected: Boolean
): ReactionView = ReactionView(context).apply { setEmoji(emojiUnicode, count, isSelected) }
