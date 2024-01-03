package com.study.components.util

import android.content.Context
import javax.inject.Inject

class AndroidResourcesProvider @Inject constructor(private val context: Context) :
    ResourcesProvider {
    override fun getString(stringResId: Int): String {
        return context.getString(stringResId)
    }

    override fun getString(stringResId: Int, args: String): String {
        return context.getString(stringResId, args)
    }

}