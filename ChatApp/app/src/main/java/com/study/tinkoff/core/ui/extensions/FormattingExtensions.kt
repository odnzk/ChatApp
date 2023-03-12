package com.study.tinkoff.core.ui.extensions

fun String.toEmojiString(): String {
    return String(Character.toChars(Integer.decode("0x$this")))
}
