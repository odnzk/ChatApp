package com.study.common.extensions

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

inline fun <T> fastLazy(crossinline initializer: () -> T): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE) { initializer() }

inline fun <T> toFlow(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    crossinline action: suspend () -> T
): Flow<T> = flow { emit(action()) }.flowOn(dispatcher)
