package com.study.tinkoff.common


interface ResourceProvider {
    fun getColor(colorId: Int): Int
    fun getString(stringId: Int): String
}
