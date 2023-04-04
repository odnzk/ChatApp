package com.study.chat.presentation.chat.model

import java.util.Calendar

internal sealed class UiMessage(
    open val id: Int,
    open val content: String,
    open val calendar: Calendar,
    open val reactions: List<UiReaction>
) {
    data class ChatMessage(
        override val id: Int,
        override val content: String,
        override val calendar: Calendar,
        override val reactions: List<UiReaction>,
        val senderAvatarUrl: String,
        val senderName: String
    ) : UiMessage(id, content, calendar, reactions)

    data class MeMessage(
        override val content: String,
        override var id: Int = NOT_YET_SYNCHRONIZED_ID,
        override val calendar: Calendar = Calendar.getInstance(),
        override val reactions: List<UiReaction> = emptyList()
    ) : UiMessage(id, content, calendar, reactions)

    companion object {
        const val NOT_YET_SYNCHRONIZED_ID = -1
    }
}
