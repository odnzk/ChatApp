package com.study.chat.chat.presentation.util.mapper

import com.study.chat.chat.presentation.model.UiEmoji
import com.study.chat.common.domain.model.Emoji

internal fun Emoji.toUiEmoji(): UiEmoji = UiEmoji(name = name, code = code)

internal fun UiEmoji.toEmoji(): Emoji = Emoji(name = name, code = code)

internal fun List<Emoji>.toUiEmojis(): List<UiEmoji> = map { it.toUiEmoji() }
