package com.study.common.validation

interface Validator<T : Any> {
    fun validate(item: T)
}
