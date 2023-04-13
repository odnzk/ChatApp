package com.study.common.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

inline fun <T> fastLazy(crossinline initializer: () -> T): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE) { initializer() }

inline fun <T> toFlow(crossinline action: suspend () -> T): Flow<T> = flow { emit(action()) }
