package com.study.network.util

fun String.toJsonArrayString(): String =
    listOf(this).joinToString(prefix = "[", postfix = "]")
