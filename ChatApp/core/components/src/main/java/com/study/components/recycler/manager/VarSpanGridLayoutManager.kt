package com.study.components.recycler.manager

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

class VarSpanGridLayoutManager(
    context: Context?, private val maxColumnWidth: Int, private val minSpanCount: Int
) : GridLayoutManager(context, minSpanCount) {
    override fun onLayoutChildren(
        recycler: Recycler, state: RecyclerView.State
    ) {
        var gridCount: Int = width / maxColumnWidth
        if (gridCount < minSpanCount) gridCount = minSpanCount
        spanCount = gridCount
        super.onLayoutChildren(recycler, state)
    }
}
