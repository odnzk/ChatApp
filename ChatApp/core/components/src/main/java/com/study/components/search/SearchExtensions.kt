package com.study.components.search

import com.study.components.extensions.NothingFoundForThisQueryException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest

inline fun <T> List<T>.searchFlow(query: String, predicate: (T) -> Boolean): List<T> =
    if (query.isEmpty()) {
        this
    } else {
        filter { predicate(it) }.takeIf { it.isNotEmpty() }
            ?: throw NothingFoundForThisQueryException()
    }


@OptIn(ExperimentalCoroutinesApi::class)
inline fun <T> Flow<List<T>>.searchFlow(
    query: String, crossinline predicate: (T) -> Boolean
): Flow<List<T>> = mapLatest { list ->
    if (query.isEmpty()) {
        list
    } else list.filter { predicate(it) }.takeIf { it.isNotEmpty() }
        ?: throw NothingFoundForThisQueryException()
}.distinctUntilChanged()




