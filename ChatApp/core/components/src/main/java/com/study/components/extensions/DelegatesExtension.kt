package com.study.components.extensions

import androidx.recyclerview.widget.RecyclerView
import com.study.components.recycler.delegates.Delegate

fun delegatesToList(vararg delegates: Delegate<*, *>): List<Delegate<RecyclerView.ViewHolder, Any>> {
    return delegates.toList() as? List<Delegate<RecyclerView.ViewHolder, Any>>
        ?: error("Cannot create correct list from this delegates")
}
