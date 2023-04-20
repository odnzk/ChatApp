package com.study.common.extensions

fun String.toEmojiString(): String = runCatching {
    String(Character.toChars(Integer.decode("0x$this")))
}.getOrDefault(this)
