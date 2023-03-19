package com.study.tinkoff.feature.chat.presentation.model

import com.study.domain.model.Reaction
import java.util.Calendar

sealed class UiMessage(
    open val id: Int,
    open val content: String,
    open val calendar: Calendar,
    open val reactions: List<Reaction>
) {
    data class ChatMessage(
        override val id: Int,
        override val content: String,
        override val calendar: Calendar,
        override val reactions: List<Reaction>,
        val senderAvatarUrl: String?,
        val senderName: String
    ) : UiMessage(id, content, calendar, reactions)

    data class MeMessage(
        override val id: Int,
        override val content: String,
        override val calendar: Calendar,
        override val reactions: List<Reaction>
    ) : UiMessage(id, content, calendar, reactions)
}
