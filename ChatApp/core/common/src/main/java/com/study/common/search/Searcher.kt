package com.study.common.search

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest

/**
 * A generic interface that provides methods for filtering a list of objects based on a search query.
 *
 * @param T The type of object being searched
 */
interface Searcher<T> {
    /**
     * A function that takes an object of type [T] and a search query, and returns a Boolean value
     * indicating whether the object matches the search query or not.
     */
    val searchPredicate: (T, String) -> Boolean

    /**
     * Returns a new list of objects that match the provided search query using the [searchPredicate]
     * function passed in. If the query is empty, the original list is returned.
     *
     * @param query The search query string
     * @param list The list of objects to search within
     * @return A new list of objects matching the search query, or the original list if the query is empty
     * @throws NothingFoundForThisQueryException if the resulting list is empty after filtering
     */
    fun toSearchList(query: String, list: List<T>): List<T> = if (query.isEmpty()) {
        list
    } else {
        list.filter { searchPredicate(it, query) }.takeIf { it.isNotEmpty() }
            ?: throw NothingFoundForThisQueryException()
    }


    /**
     * Returns a new flow of lists of objects that match the provided search query using the [searchPredicate]
     * function passed in. If the query is empty, a flow of the original list is returned.
     *
     * @param query The search query string
     * @param flow The flow of lists of objects to search within
     * @return A new flow of lists of objects matching the search query, or a flow of the original list if the query is empty
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun toSearchFlow(query: String, flow: Flow<List<T>>): Flow<List<T>> = flow.mapLatest { list ->
        if (query.isEmpty()) {
            list
        } else {
            list.filter { searchPredicate(it, query) }.takeIf { it.isNotEmpty() }
                ?: throw NothingFoundForThisQueryException()
        }
    }.distinctUntilChanged()
}


