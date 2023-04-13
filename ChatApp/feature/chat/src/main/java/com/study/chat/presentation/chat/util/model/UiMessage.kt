package com.study.chat.presentation.chat.util.model

import java.util.Calendar

internal sealed class UiMessage(
    open val id: Int,
    open val content: String,
    open val calendar: Calendar,
    open val reactions: List<UiReaction>
) {
    abstract fun baseCopy(
        id: Int = this.id,
        content: String = this.content,
        calendar: Calendar = this.calendar,
        reactions: List<UiReaction> = this.reactions
    ): UiMessage

    data class ChatMessage(
        override val id: Int,
        override val content: String,
        override val calendar: Calendar,
        override val reactions: List<UiReaction>,
        val senderAvatarUrl: String,
        val senderName: String
    ) : UiMessage(id, content, calendar, reactions) {
        override fun baseCopy(
            id: Int,
            content: String,
            calendar: Calendar,
            reactions: List<UiReaction>
        ): ChatMessage = copy(
            id = id,
            content = content,
            calendar = calendar,
            reactions = reactions,
            senderAvatarUrl = senderAvatarUrl,
            senderName = senderName
        )
    }

    data class MeMessage(
        override val content: String,
        override var id: Int = NOT_YET_SYNCHRONIZED_ID,
        override val calendar: Calendar = Calendar.getInstance(),
        override val reactions: List<UiReaction> = emptyList()
    ) : UiMessage(id, content, calendar, reactions) {
        override fun baseCopy(
            id: Int,
            content: String,
            calendar: Calendar,
            reactions: List<UiReaction>
        ): MeMessage = copy(
            id = id,
            content = content,
            calendar = calendar,
            reactions = reactions
        )
    }

    companion object {
        const val NOT_YET_SYNCHRONIZED_ID = -1
    }
}
