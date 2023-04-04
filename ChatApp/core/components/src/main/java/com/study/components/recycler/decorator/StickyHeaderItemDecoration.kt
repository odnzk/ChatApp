package com.study.components.recycler.decorator

import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class StickyHeaderItemDecoration<HeaderViewHolder : RecyclerView.ViewHolder> :
    RecyclerView.ItemDecoration() {
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val topChild = parent.getChildAt(0) ?: return
        val topChildPosition = parent.getChildAdapterPosition(topChild)
        if (topChildPosition == -1) return
        val header = parent.createHeaderView(topChildPosition) ?: parent.createPreviousHeaderView(
            topChildPosition
        ) ?: return
        parent.makeViewFullLayout(header)
        val nextHeader = parent.findNextHeaderView()

        val dx = if (nextHeader != null) {
            val differ = nextHeader.top.toFloat() - header.height
            if (differ <= 0) differ else 0f
        } else {
            0f
        }
        c.run {
            save()
            translate(0f, dx)
            header.draw(this)
            restore()
        }
    }

    private fun RecyclerView.createHeaderView(position: Int): View? {
        val adapter = adapter ?: return null
        val holder = adapter.onCreateViewHolder(
            this, adapter.getItemViewType(position)
        ) as? HeaderViewHolder ?: return null
        adapter.onBindViewHolder(holder, position)
        return holder.itemView
    }

    private fun RecyclerView.createPreviousHeaderView(position: Int): View? {
        if (position <= 0) return null
        for (i in position downTo 0) {
            createHeaderView(i)?.let { return it }
        }
        return null
    }

    private fun RecyclerView.findNextHeaderView(): View? {
        val itemCount = adapter?.itemCount ?: 0
        if (itemCount <= 1) {
            return null
        }
        val visibleCount = layoutManager?.childCount ?: return null
        for (i in 1..visibleCount) {
            val header = getChildAt(i)
            if (header != null && getChildViewHolder(header) as? HeaderViewHolder != null) {
                return header
            }
        }
        return null
    }

    private fun RecyclerView.makeViewFullLayout(view: View) {
        val parentWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val parentHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)

        val childLayoutParam = view.layoutParams ?: return

        val childWidth = ViewGroup.getChildMeasureSpec(parentWidth, 0, childLayoutParam.width)
        val childHeight = ViewGroup.getChildMeasureSpec(parentHeight, 0, childLayoutParam.height)

        view.measure(childWidth, childHeight)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }
}
