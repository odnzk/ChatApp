package com.study.chat.presentation.util.mapper

import com.study.chat.domain.model.Emoji
import com.study.chat.presentation.util.model.UiEmoji

internal fun Emoji.toUiEmoji(): UiEmoji = UiEmoji(name = name, code = code)

internal fun UiEmoji.toEmoji(): Emoji = Emoji(name = name, code = code)

internal fun List<Emoji>.toUiEmojis(): List<UiEmoji> = map { it.toUiEmoji() }
