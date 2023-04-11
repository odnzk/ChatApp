package com.study.common.extensions

fun String.toEmojiString(): String {
    return try {
        String(Character.toChars(Integer.decode("0x$this")))
    } catch (e: java.lang.NumberFormatException) {
        this
    }
}
