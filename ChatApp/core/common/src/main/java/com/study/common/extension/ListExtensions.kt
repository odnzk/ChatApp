package com.study.common.extension

inline fun <reified T> List<*>.firstInstance(): T = first { it is T } as T
