package com.study.components.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Region
import android.os.Build
import android.util.AttributeSet
import com.google.android.material.R
import com.google.android.material.color.MaterialColors
import com.google.android.material.imageview.ShapeableImageView
import com.study.components.model.UiUserPresenceStatus
import java.lang.Integer.min

class AvatarImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ShapeableImageView(context, attrs, defStyleAttr) {

    private val subtractionCirclePath = Path()
    private var circleRadius: Float = 0f
    private var subtractionCircleRadius: Float = 0f
    private var circleStart: Float = 0f
    private var circleTop: Float = 0f
    private val activeColor = getColor(UiUserPresenceStatus.ACTIVE)
    private val idleColor = getColor(UiUserPresenceStatus.IDLE)
    private val offlineColor = getColor(UiUserPresenceStatus.OFFLINE)
    private val botColor = getColor(UiUserPresenceStatus.BOT)
    private var circlePaint: Paint = Paint()
    private val initColor = MaterialColors.getColor(
        context, R.attr.backgroundColor, context.getColor(com.study.ui.R.color.dark_nero)
    )
    var status: UiUserPresenceStatus? = null
        set(value) {
            field = value
            when (status) {
                UiUserPresenceStatus.ACTIVE -> activeColor
                UiUserPresenceStatus.IDLE -> idleColor
                UiUserPresenceStatus.OFFLINE -> offlineColor
                UiUserPresenceStatus.BOT -> botColor
                else -> initColor
            }.also { circlePaint.color = it }
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

    private fun getColor(status: UiUserPresenceStatus): Int {
        return context.getColor(status.colorResId)
    }

    companion object {
        private const val CIRCLE_SIZE_DIVIDER = 7
        private const val SUBTRACTION_CIRCLE_SIZE_COEFF = 1.4f
    }
}
