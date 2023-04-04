package com.study.components.recycler.shimmer


interface ShimmerItem<T : Any> {
    fun content(): T?
}

