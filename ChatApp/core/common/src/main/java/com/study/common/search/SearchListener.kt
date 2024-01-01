package com.study.common.search

fun interface SearchListener {
    fun onNewQuery(query: String)
}