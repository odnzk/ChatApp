package com.study.chat.presentation.chat

internal sealed interface ChatFragmentEvent {
    object Reload : ChatFragmentEvent
    class SendMessage(val messageContent: String) : ChatFragmentEvent
    class UpdateReaction(val messageId: Int, val emojiName: String) : ChatFragmentEvent
}
