package com.study.common.extension

fun <T> MutableList<T>.update(index: Int, item: T): List<T> = apply { this[index] = item }

inline fun <T> List<T>.findWithIndex(predicate: (T) -> Boolean): Pair<T?, Int?> {
    for ((i, element) in this.withIndex()) if (predicate(element)) return element to i
    return null to null
}

inline fun <T> MutableList<T>.replaceFirst(replacement: T, predicate: (T) -> Boolean): List<T> {
    for ((i, element) in this.withIndex()) if (predicate(element)) set(i, replacement)
    return this
}

inline fun <reified T> List<*>.firstInstance(): T = first { it is T } as T
