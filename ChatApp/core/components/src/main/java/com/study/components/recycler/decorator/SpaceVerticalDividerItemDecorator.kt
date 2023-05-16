package com.study.components.recycler.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceVerticalDividerItemDecorator(private val spacingDp: Int) :
    RecyclerView.ItemDecoration() {

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
        val oneSideDivider = spacingDp / 2
        with(outRect) {
            if (currentPosition != state.itemCount - 1) {
                top = oneSideDivider
                bottom = oneSideDivider
            } else {
                setEmpty()
            }
        }
    }
}
