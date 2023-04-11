package com.study.components.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Region
import android.os.Build
import android.util.AttributeSet
import com.google.android.material.imageview.ShapeableImageView
import com.study.components.model.UserPresenceStatus
import java.lang.Integer.min

class AvatarImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ShapeableImageView(context, attrs, defStyleAttr) {

    private val subtractionCirclePath = Path()
    private var circleRadius: Float = 0f
    private var subtractionCircleRadius: Float = 0f
    private var circleStart: Float = 0f
    private var circleTop: Float = 0f
    private val activeColor = getColor(UserPresenceStatus.ACTIVE)
    private val idleColor = getColor(UserPresenceStatus.IDLE)
    private val offlineColor = getColor(UserPresenceStatus.OFFLINE)
    private var circlePaint: Paint = Paint()
    var status: UserPresenceStatus? = null
        set(value) {
            field = value
            when (status) {
                UserPresenceStatus.ACTIVE -> activeColor
                UserPresenceStatus.IDLE -> idleColor
                UserPresenceStatus.OFFLINE -> offlineColor
                else -> null
            }?.let { circlePaint.color = it }
            invalidate()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val minSideSize =
            min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec))
        circleRadius = (minSideSize / CIRCLE_SIZE_DIVIDER).toFloat()
        subtractionCircleRadius = circleRadius * SUBTRACTION_CIRCLE_SIZE_COEFF
        circleStart = measuredWidth - circleRadius
        circleTop = measuredHeight - circleRadius
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.save()
        subtractionCirclePath.rewind()
        subtractionCirclePath.addCircle(
            circleStart,
            circleTop,
            subtractionCircleRadius,
            Path.Direction.CCW
        )
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            canvas?.clipPath(subtractionCirclePath, Region.Op.DIFFERENCE)
        } else {
            canvas?.clipOutPath(subtractionCirclePath)
        }
        super.onDraw(canvas)
        canvas?.restore()
        canvas?.drawCircle(circleStart, circleTop, circleRadius, circlePaint)
    }

    companion object {
        private const val CIRCLE_SIZE_DIVIDER = 7
        private const val SUBTRACTION_CIRCLE_SIZE_COEFF = 1.4f
    }

    private fun getColor(status: UserPresenceStatus): Int {
        return context.getColor(status.colorResId)
    }
}
