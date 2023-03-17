package com.study.tinkoff.feature.chat.presentation.delegates.message

import com.study.tinkoff.core.ui.rv.delegates.DelegateItem
import com.study.tinkoff.feature.chat.presentation.model.UiMessage

class MessageDelegateItem(private val message: UiMessage) : DelegateItem<UiMessage> {
    override fun content(): UiMessage = message

    override fun id(): Int = message.id

    override fun compareToOther(other: UiMessage): Boolean = message == other

}
