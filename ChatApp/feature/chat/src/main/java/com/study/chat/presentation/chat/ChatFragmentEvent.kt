package com.study.chat.presentation.chat

internal sealed interface ChatFragmentEvent {
    object Reload : ChatFragmentEvent
    class SendMessage(
        val messageContent: String,
        val currList: List<Any?>
    ) : ChatFragmentEvent

    class UpdateReaction(
        val messageId: Int,
        val emojiName: String,
        val currList: List<Any?>
    ) : ChatFragmentEvent

    class Search(val query: String) : ChatFragmentEvent
}
