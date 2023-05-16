package com.study.components.recycler.manager

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

/**
 * A custom layout manager for a [RecyclerView] that supports variable span sizes.
 * This layout manager extends the default [GridLayoutManager] by dynamically adjusting the number
 * of columns based on the container width and a maximum column width constraint.
 *
 * @param context The [Context] used to create the layout manager.
 * @param maxColumnWidth The maximum width of a column in pixels.
 * @param minSpanCount The minimum number of columns to display.
 */
class VarSpanGridLayoutManager(
    context: Context?, private val maxColumnWidth: Int, private val minSpanCount: Int
) : GridLayoutManager(context, minSpanCount) {

    /**
     * Overrides the default layout calculation method to adjust the number of columns used
     * based on the container width and maximum column width constraints.
     *
     * @param recycler A [Recycler] used to hold the list items.
     * @param state The current state of the [RecyclerView].
     */
    override fun onLayoutChildren(
        recycler: Recycler, state: RecyclerView.State
    ) {
        var gridCount: Int = width / maxColumnWidth
        if (gridCount < minSpanCount) gridCount = minSpanCount
        spanCount = gridCount
        super.onLayoutChildren(recycler, state)
    }
}
