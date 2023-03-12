package com.study.tinkoff.feature.select_emoji.presentation.delegates

import com.study.tinkoff.core.domain.model.Emoji
import com.study.tinkoff.core.ui.rv.delegates.DelegateItem

class EmojiDelegateItem(private val emoji: Emoji) : DelegateItem<Emoji> {
    override fun content(): Emoji = emoji

    override fun id(): Int = emoji.hashCode()

    override fun compareToOther(other: Emoji): Boolean = emoji == other
}
