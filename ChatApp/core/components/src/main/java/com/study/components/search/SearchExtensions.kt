package com.study.components.search

import com.study.components.extensions.NothingFoundForThisQueryException

inline fun <T> List<T>.searchNotEmptyQuery(query: String, predicate: (T) -> Boolean): List<T> =
    if (query.isEmpty()) {
        this
    } else {
        filter { predicate(it) }.takeIf { it.isNotEmpty() }
            ?: throw NothingFoundForThisQueryException()
    }



