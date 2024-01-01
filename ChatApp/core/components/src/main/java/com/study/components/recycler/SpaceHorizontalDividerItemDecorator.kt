package com.study.components.recycler

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceHorizontalDividerItemDecorator(spacingDp: Int) :
    RecyclerView.ItemDecoration() {

    private val oneSideDivider = spacingDp / SIDE_DIVIDER

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val viewHolder = parent.getChildViewHolder(view)
        val currentPosition = parent.getChildAdapterPosition(view)
            .takeIf { it != RecyclerView.NO_POSITION } ?: viewHolder.oldPosition

        with(outRect) {
            when (currentPosition) {
                0 -> right = oneSideDivider
                state.itemCount - 1 -> left = oneSideDivider
                else -> {
                    right = oneSideDivider
                    left = oneSideDivider
                }
            }
        }
    }

    companion object {
        private const val SIDE_DIVIDER = 2
    }
}
