package com.study.chat.chat.presentation.util.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.ColorUtils
import androidx.core.view.setPadding
import com.google.android.material.color.MaterialColors
import com.study.chat.R
import com.study.components.ext.sp
import kotlin.math.roundToInt
import com.google.android.material.R as MaterialR
import com.study.ui.R as CoreR

internal class ReactionView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var paddings = DEFAULT_PADDING
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = TEXT_SIZE.sp(context)
        color = MaterialColors.getColor(
            context,
            MaterialR.attr.colorOnBackground,
            context.getColor(CoreR.color.white)
        )
    }
    private val textBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val textBounds = Rect()
    private val textBackground = RectF()
    private val viewNotSelectedColor = MaterialColors.getColor(
        context,
        MaterialR.attr.backgroundColor,
        context.getColor(CoreR.color.dark_nero)
    )
    private val viewSelectedColor =
        ColorUtils.blendARGB(viewNotSelectedColor, Color.GRAY, BLEND_COEFFICIENT)
    private var textToDraw = ""

    init {
        context.withStyledAttributes(attrs, R.styleable.EmojiView) {
            val reaction = this.getString(R.styleable.EmojiView_emoji)
            val count =
                this.getInt(R.styleable.EmojiView_emoji_count, DEFAULT_EMOJI_COUNT)
            paddings = this.getDimension(R.styleable.EmojiView_emoji_padding, DEFAULT_PADDING)
            reaction?.let { textToDraw = toEmoji(it, count) }
        }
        setPadding(paddings.roundToInt())
    }

    fun setEmoji(emojiUnicode: String, count: Int = DEFAULT_EMOJI_COUNT, isSelected: Boolean) {
        textToDraw = toEmoji(emojiUnicode, count)
        this.isSelected = isSelected
        manageBackgroundColor(isSelected)
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(textToDraw, 0, textToDraw.length, textBounds)

        val totalWidth = textBounds.width() + paddingRight + paddingLeft
        val totalHeight = textBounds.height() + paddingTop + paddingBottom

        val measuredWidth = resolveSize(totalWidth, widthMeasureSpec)
        val measuredHeight = resolveSize(totalHeight, heightMeasureSpec)

        textBackground.set(0f, 0f, totalWidth.toFloat(), totalHeight.toFloat())
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        val centerY = height / 2 - textBounds.exactCenterY()
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
            manageBackgroundColor(isSelected)
            l?.onClick(it)
        }
    }

    private fun toEmoji(reaction: String, count: Int): String {
        return when {
            count > 0 -> "$reaction $count"
            else -> reaction
        }
    }

    private fun manageBackgroundColor(isSelected: Boolean) {
        textBackgroundPaint.color = if (isSelected) viewSelectedColor else viewNotSelectedColor
    }

    companion object {
        private const val ROUND_CORNERS_SIZE = 32f
        private const val DEFAULT_EMOJI_COUNT = 0
        private const val TEXT_SIZE = 14f
        private const val BLEND_COEFFICIENT = 0.2f
        private const val DEFAULT_PADDING = 12f
    }
}
