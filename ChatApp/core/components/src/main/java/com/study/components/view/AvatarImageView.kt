package com.study.components.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import com.google.android.material.color.MaterialColors
import com.google.android.material.imageview.ShapeableImageView
import java.lang.Integer.min
import com.study.ui.R as CoreResources

class AvatarImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ShapeableImageView(context, attrs, defStyleAttr) {

    private var circleRadius: Float = 0f
    private var circleStart: Float = 0f
    private var circleTop: Float = 0f
    private val circlePaint = Paint().apply {
        color = context.getColor(com.study.ui.R.color.light_green)
    }
    private var circleStrokePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = MaterialColors.getColor(
            context,
            com.google.android.material.R.attr.backgroundColor,
            context.getColor(CoreResources.color.item_background_color)
        )
    }
    var status: Status = Status.OFFLINE

    enum class Status {
        ONLINE, OFFLINE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (status == Status.ONLINE) {
            val minSideSize =
                min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec))
            circleRadius = (minSideSize / CIRCLE_SIZE_DIVIDER).toFloat()
            circleStrokePaint.strokeWidth = circleRadius / CIRCLE_STROKE_SIZE_DIVIDER
            circleStart = measuredWidth - circleRadius - circleStrokePaint.strokeWidth
            circleTop = measuredHeight - circleRadius - circleStrokePaint.strokeWidth
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (status == Status.ONLINE) {
            canvas?.drawCircle(circleStart, circleTop, circleRadius, circlePaint)
            canvas?.drawCircle(circleStart, circleTop, circleRadius, circleStrokePaint)
        }
    }

    companion object {
        private const val CIRCLE_SIZE_DIVIDER = 7
        private const val CIRCLE_STROKE_SIZE_DIVIDER = 4
    }
}
