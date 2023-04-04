package com.study.components.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import com.google.android.material.color.MaterialColors
import com.google.android.material.imageview.ShapeableImageView
import com.study.components.model.UserPresenceStatus
import java.lang.Integer.min
import com.google.android.material.R as AndroidR
import com.study.ui.R as CoreR

class AvatarImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ShapeableImageView(context, attrs, defStyleAttr) {

    private var circleRadius: Float = 0f
    private var circleStart: Float = 0f
    private var circleTop: Float = 0f
    private val activeColor = getColor(UserPresenceStatus.ACTIVE)
    private val idleColor = getColor(UserPresenceStatus.IDLE)
    private val offlineColor = getColor(UserPresenceStatus.OFFLINE)
    private val strokeColor = MaterialColors.getColor(
        context,
        AndroidR.attr.backgroundColor,
        context.getColor(CoreR.color.item_background_color)
    )
    private var circlePaint: Paint = Paint()
    private var circleStrokePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = strokeColor
    }
    var status: UserPresenceStatus? = null
        set(value) {
            field = value
            invalidate()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val minSideSize =
            min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec))
        circleRadius = (minSideSize / CIRCLE_SIZE_DIVIDER).toFloat()
        circleStrokePaint.strokeWidth = circleRadius / CIRCLE_STROKE_SIZE_DIVIDER
        circleStart = measuredWidth - circleRadius - circleStrokePaint.strokeWidth
        circleTop = measuredHeight - circleRadius
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        circlePaint.color = when (status) {
            UserPresenceStatus.ACTIVE -> activeColor
            UserPresenceStatus.IDLE -> idleColor
            UserPresenceStatus.OFFLINE -> offlineColor
            else -> strokeColor
        }
        canvas?.drawCircle(circleStart, circleTop, circleRadius, circlePaint)
        canvas?.drawCircle(circleStart, circleTop, circleRadius, circleStrokePaint)
    }

    companion object {
        private const val CIRCLE_SIZE_DIVIDER = 7
        private const val CIRCLE_STROKE_SIZE_DIVIDER = 3
    }

    private fun getColor(status: UserPresenceStatus): Int {
        return context.getColor(status.colorResId)
    }
}
