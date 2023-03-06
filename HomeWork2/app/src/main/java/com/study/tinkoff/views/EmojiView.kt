package com.study.tinkoff.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import androidx.core.view.setPadding
import com.study.tinkoff.R
import com.study.tinkoff.common.extensions.sp


class EmojiView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 14f.sp(context)
        color = Color.WHITE
    }

    private val textBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.item_background_color)
        style = Paint.Style.FILL
    }

    private val textBounds = Rect()
    private val textBackground = RectF()
    private var textToDraw = ""

    fun setEmoji(emojiUnicode: String, count: Int = DEFAULT_EMOJI_COUNT) {
        textToDraw = toEmoji(emojiUnicode, count)
    }

    var onEmojiViewClickListener: (() -> Unit)? = null
        set(value) {
            field = value
            setOnClickListener {
                isSelected = !isSelected
                field?.invoke()
            }
        }

    init {
        context.withStyledAttributes(attrs, R.styleable.EmojiView) {
            val reaction = this.getString(R.styleable.EmojiView_emoji)
            val count = this.getInt(R.styleable.EmojiView_emoji_count, DEFAULT_EMOJI_COUNT)
            textToDraw = toEmoji(reaction, count)
        }
        if (paddingBottom == 0 && paddingTop == 0 && paddingRight == 0 && paddingLeft == 0) {
            setPadding(DEFAULT_PADDINGS)
        }
        onEmojiViewClickListener = {}
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(textToDraw, 0, textToDraw.length, textBounds)

        val totalWidth = textBounds.width() + paddingRight + paddingLeft
        val totalHeight = textBounds.height() + paddingTop + paddingBottom

        val measuredWidth = resolveSize(totalWidth, widthMeasureSpec)
        val measuredHeight = resolveSize(totalHeight, heightMeasureSpec)

        textBackground.set(
            0f,
            (measuredHeight / 2 - textBounds.height() / 2 - paddingTop).toFloat(),
            totalWidth.toFloat(),
            (measuredHeight / 2 + textBounds.height() / 2 + paddingBottom).toFloat()
        )
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        val centerY = height / 2 - textBounds.exactCenterY()
        textBackgroundPaint.color =
            if (isSelected) context.getColor(R.color.item_background_selected_color) else
                context.getColor(R.color.item_background_color)

        canvas.drawRoundRect(
            textBackground, ROUND_CORNERS_SIZE, ROUND_CORNERS_SIZE, textBackgroundPaint
        )
        canvas.drawText(textToDraw, paddingRight.toFloat(), centerY, textPaint)
    }

    companion object {
        private const val ROUND_CORNERS_SIZE = 32f
        private const val DEFAULT_EMOJI_COUNT = 0
        private const val DEFAULT_PADDINGS = 12
    }


    private fun toEmoji(reaction: String?, count: Int): String {
        return when {
            !reaction.isNullOrBlank() && count > 0 -> "$reaction $count"
            !reaction.isNullOrBlank() -> "$reaction"
            else -> ""
        }
    }
}
