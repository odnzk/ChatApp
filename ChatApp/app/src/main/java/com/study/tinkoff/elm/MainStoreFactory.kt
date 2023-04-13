package com.study.tinkoff.elm

import kotlinx.coroutines.CoroutineDispatcher
import vivid.money.elmslie.coroutines.ElmStoreCompat

internal class MainStoreFactory(private val dispatcher: CoroutineDispatcher) {
    private val store by lazy {
        ElmStoreCompat(MainState(), MainReducer(), MainActor(dispatcher))
    }

    fun create() = store
}
