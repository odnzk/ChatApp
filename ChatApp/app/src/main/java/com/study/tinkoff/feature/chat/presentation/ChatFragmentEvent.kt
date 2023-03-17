package com.study.tinkoff.feature.chat.presentation

sealed interface ChatFragmentEvent {
    object ReloadData : ChatFragmentEvent
    class SendMessage(val messageContent: String) : ChatFragmentEvent
    class UpdateReaction(val messageId: Int, val emojiName: String) : ChatFragmentEvent
}
