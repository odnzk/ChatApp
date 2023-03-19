package com.study.components.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import androidx.core.view.setPadding
import com.google.android.material.color.MaterialColors
import com.study.common.extensions.toEmojiString
import com.study.components.sp
import kotlin.math.min
import kotlin.math.roundToInt
import com.study.ui.R as CoreResources

class ReactionView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = TEXT_SIZE.sp(context)
        color = MaterialColors.getColor(
            context,
            com.google.android.material.R.attr.colorOnBackground,
            context.getColor(CoreResources.color.white)
        )
    }
    private val textBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = MaterialColors.getColor(
            context,
            com.google.android.material.R.attr.backgroundColor,
            context.getColor(CoreResources.color.item_background_color)
        )
        style = Paint.Style.FILL
    }
    private val textBounds = Rect()
    private val textBackground = RectF()
    private val viewNotSelectedColor = MaterialColors.getColor(
        context,
        com.google.android.material.R.attr.backgroundColor,
        context.getColor(CoreResources.color.item_background_color)
    )
    private val viewSelectedColor = viewNotSelectedColor.makeColorDarker(1f)
    private var textToDraw = ""

    init {
        context.withStyledAttributes(attrs, CoreResources.styleable.EmojiView) {
            val reaction = this.getString(CoreResources.styleable.EmojiView_emoji)
            val count =
                this.getInt(CoreResources.styleable.EmojiView_emoji_count, DEFAULT_EMOJI_COUNT)
            reaction?.let { textToDraw = toEmoji(it, count) }
        }
        if (paddingBottom == 0 && paddingTop == 0 && paddingRight == 0 && paddingLeft == 0) {
            setPadding(DEFAULT_PADDINGS)
        }
    }

    fun setEmoji(emojiUnicode: String, count: Int = DEFAULT_EMOJI_COUNT) {
        textToDraw = toEmoji(emojiUnicode.toEmojiString(), count)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(textToDraw, 0, textToDraw.length, textBounds)

        val totalWidth = textBounds.width() + paddingRight + paddingLeft
        val totalHeight = textBounds.height() + paddingTop + paddingBottom

        val measuredWidth = resolveSize(totalWidth, widthMeasureSpec)
        val measuredHeight = resolveSize(totalHeight, heightMeasureSpec)

        textBackground.set(
            0f, 0f, totalWidth.toFloat(), totalHeight.toFloat()
        )
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        val centerY = height / 2 - textBounds.exactCenterY()
        textBackgroundPaint.color = if (isSelected) viewSelectedColor else viewNotSelectedColor
        canvas.run {
            drawRoundRect(
                textBackground, ROUND_CORNERS_SIZE, ROUND_CORNERS_SIZE, textBackgroundPaint
            )
            drawText(textToDraw, paddingRight.toFloat(), centerY, textPaint)
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener {
            isSelected = !isSelected
            l?.onClick(it)
        }
    }

    companion object {
        private const val ROUND_CORNERS_SIZE = 32f
        private const val DEFAULT_EMOJI_COUNT = 0
        private const val DEFAULT_PADDINGS = 12
        private const val TEXT_SIZE = 14f
    }

    private fun toEmoji(reaction: String, count: Int): String {
        return when {
            count > 0 -> "$reaction $count"
            else -> reaction
        }
    }

    private fun Int.makeColorDarker(factor: Float): Int {
        val a: Int = Color.alpha(this)
        val r = (Color.red(this) * factor).roundToInt()
        val g = (Color.green(this) * factor).roundToInt()
        val b = (Color.blue(this) * factor).roundToInt()
        return Color.argb(
            a, min(r, 255), min(g, 255), min(b, 255)
        )
    }

}
