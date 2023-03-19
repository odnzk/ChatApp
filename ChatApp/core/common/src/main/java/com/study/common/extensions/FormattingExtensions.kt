package com.study.common.extensions

fun String.toEmojiString(): String {
    return String(Character.toChars(Integer.decode("0x$this")))
}
