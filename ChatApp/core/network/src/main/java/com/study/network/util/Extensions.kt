package com.study.network.util

import com.study.network.BuildConfig

fun String.toJsonArrayString(): String =
    listOf(this).joinToString(prefix = "[", postfix = "]")

private const val BASE_URL = BuildConfig.BASE_URL
fun toUserUploadsUrl(relativePath: String) = BASE_URL.plus(relativePath)
