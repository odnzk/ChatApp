package com.study.tinkoff.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import com.study.tinkoff.R
import java.lang.Integer.max

class MessageViewGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    private val ivAvatar: ImageView
    private val tvUser: TextView
    private val tvContent: TextView
    private val flexboxEmoji: FlexBoxLayout

    var onAddReactionClickListener: () -> Unit = {}
        set(value) {
            field = value
            flexboxEmoji.onPlusClickListener = { field() }
        }

    private val textBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.item_background_color)
        style = Paint.Style.FILL
    }
    private val textBackground = RectF()

    init {
        inflate(context, R.layout.view_message, this)
        ivAvatar = findViewById(R.id.view_iv_avatar)
        tvUser = findViewById(R.id.view_tv_user)
        tvContent = findViewById(R.id.view_tv_content)
        flexboxEmoji = findViewById(R.id.view_fl_emoji)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildWithMargins(
            ivAvatar,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            0
        )
        val avatarWidth = ivAvatar.measuredWidth + ivAvatar.marginLeft + ivAvatar.marginRight
        measureChildWithMargins(tvUser, widthMeasureSpec, avatarWidth, heightMeasureSpec, 0)
        var heightUsed = tvUser.measuredHeight + tvUser.marginTop + tvUser.marginBottom
        measureChildWithMargins(
            tvContent,
            widthMeasureSpec,
            avatarWidth,
            heightMeasureSpec,
            heightUsed
        )
        heightUsed += tvContent.marginTop + tvContent.measuredHeight + tvContent.marginBottom
        measureChildWithMargins(
            flexboxEmoji,
            widthMeasureSpec,
            avatarWidth,
            heightMeasureSpec,
            heightUsed
        )
        val maxTextWidth = max(
            tvUser.measuredWidth + tvUser.marginRight + tvUser.marginLeft,
            tvContent.measuredWidth + tvContent.marginLeft + tvContent.marginRight
        )
        val tvUserHeight = tvUser.measuredHeight + tvUser.marginTop + tvUser.marginBottom
        val tvContentHeight =
            tvContent.measuredHeight + tvContent.marginTop + tvContent.marginBottom
        val flexboxHeight =
            flexboxEmoji.measuredHeight + flexboxEmoji.marginTop + flexboxEmoji.marginBottom

        val totalWidth =
            paddingLeft + paddingRight + ivAvatar.measuredWidth + ivAvatar.marginLeft + ivAvatar.marginRight + max(
                maxTextWidth,
                flexboxEmoji.measuredWidth
            )
        val totalHeight = paddingTop + paddingBottom + max(
            ivAvatar.marginTop + ivAvatar.marginBottom + ivAvatar.measuredHeight,
            tvUserHeight + tvContentHeight + flexboxHeight
        )

        val textBackgroundStart =
            (paddingLeft + ivAvatar.marginLeft + ivAvatar.measuredWidth + ivAvatar.marginRight).toFloat()
        textBackground.set(
            textBackgroundStart,
            paddingTop.toFloat(),
            textBackgroundStart + max(
                tvUser.marginLeft + tvUser.measuredWidth + tvUser.marginRight,
                tvContent.marginLeft + tvContent.measuredWidth + tvContent.marginRight
            ),
            (tvUser.marginTop + tvUser.measuredHeight + tvUser.marginBottom + tvContent.marginTop + tvContent.measuredHeight + tvContent.marginBottom).toFloat()
        )
        setMeasuredDimension(totalWidth, totalHeight)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var offsetX = paddingLeft
        var offsetY = paddingTop
        ivAvatar.layout(
            offsetX + ivAvatar.marginLeft,
            offsetY + ivAvatar.marginTop,
            offsetX + ivAvatar.measuredWidth,
            offsetY + ivAvatar.measuredHeight + ivAvatar.marginTop
        )
        offsetX += ivAvatar.measuredWidth + ivAvatar.marginRight + ivAvatar.marginLeft
        tvUser.layout(
            offsetX + tvUser.marginLeft,
            offsetY + tvUser.marginTop,
            offsetX + tvUser.measuredWidth + tvUser.marginLeft,
            offsetY + tvUser.marginTop + tvUser.measuredHeight
        )
        offsetY += tvUser.measuredHeight + tvUser.marginTop + tvUser.marginBottom
        tvContent.layout(
            offsetX + tvContent.marginLeft,
            offsetY + tvContent.marginTop,
            offsetX + tvContent.measuredWidth + tvContent.marginLeft,
            offsetY + tvContent.marginTop + tvContent.measuredHeight
        )
        offsetY += tvContent.marginTop + tvContent.measuredHeight + tvContent.marginBottom
        flexboxEmoji.layout(
            offsetX + flexboxEmoji.marginLeft,
            offsetY + flexboxEmoji.marginTop,
            offsetX + flexboxEmoji.marginLeft + flexboxEmoji.measuredWidth,
            offsetY + flexboxEmoji.marginTop + flexboxEmoji.measuredHeight
        )
    }

    override fun dispatchDraw(canvas: Canvas?) {
        canvas?.drawRoundRect(
            textBackground,
            ROUND_CORNERS_SIZE,
            ROUND_CORNERS_SIZE,
            textBackgroundPaint
        )
        super.dispatchDraw(canvas)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun checkLayoutParams(p: LayoutParams): Boolean {
        return p is MarginLayoutParams
    }

    companion object {
        private const val ROUND_CORNERS_SIZE = 18f
    }

}
