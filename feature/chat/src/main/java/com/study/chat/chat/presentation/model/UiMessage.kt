package com.study.chat.chat.presentation.model

import java.util.Calendar

internal sealed class UiMessage(
    open val id: Int,
    open val content: String,
    open val topic: String,
    open val calendar: Calendar,
    open val imageUrl: String?,
    open val reactions: List<UiReaction>
) : ChatListItem {
    abstract fun baseCopy(
        id: Int = this.id,
        content: String = this.content,
        topic: String = this.topic,
        calendar: Calendar = this.calendar,
        reactions: List<UiReaction> = this.reactions
    ): UiMessage

    data class ChatMessage(
        override val id: Int,
        override val content: String,
        override val topic: String,
        override val calendar: Calendar,
        override val reactions: List<UiReaction>,
        override val imageUrl: String?,
        val senderAvatarUrl: String,
        val senderName: String
    ) : UiMessage(
        id = id,
        content = content,
        topic = topic,
        calendar = calendar,
        imageUrl = imageUrl, reactions
    ) {
        override fun baseCopy(
            id: Int,
            content: String,
            topic: String,
            calendar: Calendar,
            reactions: List<UiReaction>
        ): ChatMessage = copy(
            id = id,
            content = content,
            topic = topic,
            calendar = calendar,
            reactions = reactions,
            senderAvatarUrl = senderAvatarUrl,
            senderName = senderName
        )
    }

    data class MeMessage(
        override val content: String,
        override var id: Int,
        override val topic: String,
        override val calendar: Calendar = Calendar.getInstance(),
        override val imageUrl: String?,
        override val reactions: List<UiReaction> = emptyList()
    ) : UiMessage(
        id = id,
        content = content,
        topic = topic,
        calendar = calendar,
        imageUrl = imageUrl,
        reactions
    ) {
        override fun baseCopy(
            id: Int,
            content: String,
            topic: String,
            calendar: Calendar,
            reactions: List<UiReaction>
        ): MeMessage = copy(
            id = id,
            topic = topic,
            content = content,
            calendar = calendar,
            reactions = reactions
        )
    }
}
