package com.study.tinkoff.feature.select_emoji.presentation.delegates

import com.study.tinkoff.core.domain.model.Emoji

fun List<Emoji>.toEmojiDelegateItems(): List<EmojiDelegateItem> = map { EmojiDelegateItem(it) }

