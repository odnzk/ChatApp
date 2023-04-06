package com.study.common.extensions

inline fun <T> List<T>.firstWithIndex(predicate: (T) -> Boolean): Pair<T, Int> {
    for ((i, element) in this.withIndex()) if (predicate(element)) return element to i
    throw NoSuchElementException("Collection contains no element matching the predicate.")
}
