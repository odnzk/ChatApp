package com.study.chat.presentation.chat.util.mapper


import com.study.chat.domain.model.IncomeMessage
import com.study.chat.presentation.chat.model.UiReaction

internal fun IncomeMessage.mapUiReactions(currentUserId: Int): List<UiReaction> {
    return reactions.groupBy { it.emoji.code }.map { group ->
        val reactions = group.value
        val isSelected = reactions.find { it.userId == currentUserId } != null
        return@map UiReaction(
            messageId = id,
            emoji = reactions.first().emoji,
            count = reactions.size,
            isSelected
        )
    }.sortedByDescending { it.count }
}


