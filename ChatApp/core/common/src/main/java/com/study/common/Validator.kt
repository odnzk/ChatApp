package com.study.common

interface Validator<T : Any> {
    fun validate(item: T)
}
