package com.study.chat.presentation.chat.util.view

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
import com.study.common.extensions.toEmojiString
import com.study.components.extensions.dp
import com.study.components.extensions.sp
import com.study.feature.R
import com.google.android.material.R as MaterialR
import com.study.ui.R as CoreR

internal class ReactionView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private val defaultPadding = 6.dp(context)

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
        ColorUtils.blendARGB(viewNotSelectedColor, Color.WHITE, LIGHTING_COEFFICIENT)
    private var textToDraw = ""

    init {
        context.withStyledAttributes(attrs, R.styleable.EmojiView) {
            val reaction = this.getString(R.styleable.EmojiView_emoji)
            val count =
                this.getInt(R.styleable.EmojiView_emoji_count, DEFAULT_EMOJI_COUNT)
            reaction?.let { textToDraw = toEmoji(it, count) }
        }
        if (paddingBottom == 0 && paddingTop == 0 && paddingRight == 0 && paddingLeft == 0) {
            setPadding(defaultPadding)
        }
    }

    fun setEmoji(emojiUnicode: String, count: Int = DEFAULT_EMOJI_COUNT, isSelected: Boolean) {
        textToDraw = toEmoji(emojiUnicode.toEmojiString(), count)
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
        private const val LIGHTING_COEFFICIENT = 0.1f
    }
}
