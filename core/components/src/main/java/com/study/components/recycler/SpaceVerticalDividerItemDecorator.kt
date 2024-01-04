package com.study.components.recycler

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceVerticalDividerItemDecorator(spacingDp: Int, private val isReversed: Boolean) :
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
            if (isReversed) {
                when (currentPosition) {
                    0 -> {
                        top = oneSideDivider
                    }

                    state.itemCount - 1 -> {
                        bottom = oneSideDivider
                    }

                    else -> {
                        top = oneSideDivider
                        bottom = oneSideDivider
                    }
                }
            } else {
                when (currentPosition) {
                    0 -> {
                        bottom = oneSideDivider
                    }

                    state.itemCount - 1 -> {
                        top = oneSideDivider
                    }

                    else -> {
                        top = oneSideDivider
                        bottom = oneSideDivider
                    }
                }
            }
        }
    }

    companion object {
        private const val SIDE_DIVIDER = 2
    }
}
