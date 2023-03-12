package com.study.tinkoff.core.ui.rv.delegates

interface DelegateItem<T : Any> {
    fun content(): T
    fun id(): Int
    fun compareToOther(other: T): Boolean
}
