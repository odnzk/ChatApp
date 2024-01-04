package com.study.common.ext

inline fun <T> fastLazy(crossinline initializer: () -> T): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE) { initializer() }

fun maxOfThee(a: Int, b: Int, c: Int) = when {
    a >= b && a >= c -> a
    b >= c -> b
    else -> c
}

inline fun <reified T> List<*>.firstInstance(): T = first { it is T } as T
