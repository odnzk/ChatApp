package com.study.chat.presentation.chat.util.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import com.google.android.material.color.MaterialColors
import com.study.chat.R
import com.study.chat.domain.model.Emoji
import com.study.chat.presentation.chat.util.mapper.toMessageEmojiViews
import com.study.chat.presentation.chat.util.model.UiMessage
import com.study.chat.presentation.chat.util.model.UiReaction
import com.study.components.extension.dp
import com.study.components.extension.loadFromUrl
import com.study.components.view.FlexBoxLayout
import java.lang.Integer.max
import com.google.android.material.R as MaterialR
import com.study.ui.R as CoreResources


internal class MessageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    private val ivAvatar: ImageView
    private val tvSender: TextView
    private val tvContent: TextView
    private val flexboxEmoji: FlexBoxLayout
    private val textBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val colorDark = MaterialColors.getColor(
        context,
        MaterialR.attr.backgroundColor,
        context.getColor(CoreResources.color.dark_nero)
    )
    private val colorAccent = MaterialColors.getColor(
        context,
        androidx.appcompat.R.attr.colorPrimary,
        context.getColor(CoreResources.color.navy_light)
    )
    private val colorDarkAccent = context.getColor(com.study.ui.R.color.navy_dark)
    private val textBackground = RectF()
    private val textBackgroundCornerRadius = 16f.dp(context).toFloat()
    private var messageType: MessageType = MessageType.CHAT_MESSAGE
    var onAddReactionClickListener = OnClickListener { }
        set(value) {
            field = value
            flexboxEmoji.onPlusClickListener = field
        }
    var maxWidthPercent: Float = 1f

    init {
        context.withStyledAttributes(attrs, R.styleable.MessageView) {
            maxWidthPercent = this.getFloat(R.styleable.MessageView_maxWidthPercent, 0f)
        }
        inflate(context, R.layout.view_message, this)
        ivAvatar = findViewById(R.id.view_message_iv_avatar)
        tvSender = findViewById(R.id.view_message_tv_user)
        tvContent = findViewById(R.id.view_message_tv_content)
        flexboxEmoji = findViewById(R.id.view_message_fl_emoji)
    }

    fun setMessage(
        message: UiMessage,
        onReactionClick: ((message: UiMessage, emoji: Emoji) -> Unit)? = null
    ) {
        when (message) {
            is UiMessage.ChatMessage -> setChatMessage(message, onReactionClick)
            is UiMessage.MeMessage -> setUserMessage(message, onReactionClick)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val allowedWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
            (MeasureSpec.getSize(widthMeasureSpec) * maxWidthPercent).toInt(),
            MeasureSpec.getMode(widthMeasureSpec)
        )
        val totalsWidthAndHeight = when (messageType) {
            MessageType.ME_MESSAGE -> {
                measureMeMessage(allowedWidthMeasureSpec, widthMeasureSpec, heightMeasureSpec)
            }
            MessageType.CHAT_MESSAGE -> measureChatMessage(
                allowedWidthMeasureSpec, widthMeasureSpec, heightMeasureSpec
            )
        }
        manageMessageBackground(totalsWidthAndHeight.first)
        setMeasuredDimension(totalsWidthAndHeight.first, totalsWidthAndHeight.second)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        when (messageType) {
            MessageType.ME_MESSAGE -> layoutMeMessage()
            MessageType.CHAT_MESSAGE -> layoutChatMessage()
        }
    }

    override fun dispatchDraw(canvas: Canvas?) {
        canvas?.drawRoundRect(
            textBackground,
            textBackgroundCornerRadius,
            textBackgroundCornerRadius,
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


    private fun manageMessageBackground(totalWidth: Int) {
        when (messageType) {
            MessageType.ME_MESSAGE -> {
                val offsetX =
                    (totalWidth - paddingRight - tvContent.measuredWidth - tvContent.marginRight - tvContent.marginLeft).toFloat()
                textBackground.set(
                    offsetX,
                    paddingTop.toFloat(),
                    totalWidth.toFloat() - paddingRight,
                    (paddingTop + tvContent.marginTop + tvContent.measuredHeight + tvContent.marginBottom).toFloat()
                )
                textBackgroundPaint.shader = LinearGradient(
                    offsetX,
                    paddingTop.toFloat(),
                    totalWidth.toFloat() - paddingRight,
                    (paddingTop + tvContent.marginTop + tvContent.measuredHeight + tvContent.marginBottom).toFloat(),
                    colorAccent,
                    colorDarkAccent,
                    Shader.TileMode.MIRROR
                )
            }
            MessageType.CHAT_MESSAGE -> {
                val textBackgroundStart =
                    (paddingLeft + ivAvatar.marginLeft + ivAvatar.measuredWidth + ivAvatar.marginRight).toFloat()
                textBackground.set(
                    textBackgroundStart,
                    paddingTop.toFloat(),
                    textBackgroundStart + max(
                        tvSender.marginLeft + tvSender.measuredWidth + tvSender.marginRight,
                        tvContent.marginLeft + tvContent.measuredWidth + tvContent.marginRight
                    ),
                    (tvSender.marginTop + tvSender.measuredHeight + tvSender.marginBottom + tvContent.marginTop + tvContent.measuredHeight + tvContent.marginBottom).toFloat()
                )
                textBackgroundPaint.shader = null
                textBackgroundPaint.color = colorDark
            }
        }
    }

    private fun measureMeMessage(
        allowedWidthMeasureSpec: Int, widthMeasureSpec: Int, heightMeasureSpec: Int
    ): Pair<Int, Int> {
        measureChildWithMargins(tvContent, allowedWidthMeasureSpec, 0, heightMeasureSpec, 0)
        measureChildWithMargins(
            flexboxEmoji,
            allowedWidthMeasureSpec,
            0,
            heightMeasureSpec,
            tvContent.marginTop + tvContent.measuredHeight + tvContent.marginBottom
        )
        val tvContentHeight =
            tvContent.measuredHeight + tvContent.marginTop + tvContent.marginBottom
        val flexboxHeight =
            flexboxEmoji.measuredHeight + flexboxEmoji.marginTop + flexboxEmoji.marginBottom
        val totalWidth = paddingLeft + paddingRight + max(
            tvContent.measuredWidth + tvContent.marginLeft + tvContent.marginRight,
            flexboxEmoji.measuredWidth
        )
        val totalHeight = paddingTop + paddingBottom + tvContentHeight + flexboxHeight
        return resolveSize(totalWidth, widthMeasureSpec) to resolveSize(
            totalHeight, heightMeasureSpec
        )
    }

    private fun measureChatMessage(
        allowedWidthMeasureSpec: Int, widthMeasureSpec: Int, heightMeasureSpec: Int
    ): Pair<Int, Int> {
        var heightUsed = 0
        var avatarWidth = 0
        measureChildWithMargins(
            ivAvatar, allowedWidthMeasureSpec, 0, heightMeasureSpec, 0
        )
        avatarWidth += ivAvatar.measuredWidth + ivAvatar.marginLeft + ivAvatar.marginRight
        measureChildWithMargins(
            tvSender, allowedWidthMeasureSpec, avatarWidth, heightMeasureSpec, 0
        )
        heightUsed += tvSender.measuredHeight + tvSender.marginTop + tvSender.marginBottom
        measureChildWithMargins(
            tvContent, allowedWidthMeasureSpec, avatarWidth, heightMeasureSpec, heightUsed
        )
        heightUsed += tvContent.marginTop + tvContent.measuredHeight + tvContent.marginBottom
        measureChildWithMargins(
            flexboxEmoji, allowedWidthMeasureSpec, avatarWidth, heightMeasureSpec, heightUsed
        )
        val maxTextWidth = max(
            tvSender.measuredWidth + tvSender.marginRight + tvSender.marginLeft,
            tvContent.measuredWidth + tvContent.marginLeft + tvContent.marginRight
        )
        val tvUserHeight = tvSender.measuredHeight + tvSender.marginTop + tvSender.marginBottom
        val tvContentHeight =
            tvContent.measuredHeight + tvContent.marginTop + tvContent.marginBottom
        val flexboxHeight =
            flexboxEmoji.measuredHeight + flexboxEmoji.marginTop + flexboxEmoji.marginBottom
        val totalWidth =
            paddingLeft + paddingRight + ivAvatar.measuredWidth + ivAvatar.marginLeft + ivAvatar.marginRight + max(
                maxTextWidth, flexboxEmoji.measuredWidth
            )
        val totalHeight = paddingTop + paddingBottom + max(
            ivAvatar.marginTop + ivAvatar.marginBottom + ivAvatar.measuredHeight,
            tvUserHeight + tvContentHeight + flexboxHeight
        )
        return resolveSize(totalWidth, widthMeasureSpec) to resolveSize(
            totalHeight, heightMeasureSpec
        )
    }

    private fun layoutChatMessage() {
        var offsetX = paddingLeft
        var offsetY = paddingTop
        ivAvatar.layout(
            offsetX + ivAvatar.marginLeft,
            offsetY + ivAvatar.marginTop,
            offsetX + ivAvatar.measuredWidth + ivAvatar.marginLeft,
            offsetY + ivAvatar.measuredHeight + ivAvatar.marginTop
        )
        offsetX += ivAvatar.measuredWidth + ivAvatar.marginRight + ivAvatar.marginLeft
        tvSender.layout(
            offsetX + tvSender.marginLeft,
            offsetY + tvSender.marginTop,
            offsetX + tvSender.measuredWidth + tvSender.marginLeft,
            offsetY + tvSender.marginTop + tvSender.measuredHeight
        )
        offsetY += tvSender.measuredHeight + tvSender.marginTop + tvSender.marginBottom
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

    private fun layoutMeMessage() {
        val offsetX = width - paddingRight
        var offsetY = paddingTop
        tvContent.layout(
            offsetX - tvContent.measuredWidth - tvContent.marginRight,
            offsetY + tvContent.marginTop,
            width - paddingRight,
            offsetY + tvContent.marginTop + tvContent.measuredHeight
        )
        offsetY += tvContent.marginTop + tvContent.measuredHeight + tvContent.marginBottom
        flexboxEmoji.layout(
            offsetX - flexboxEmoji.measuredWidth - flexboxEmoji.marginRight,
            offsetY + flexboxEmoji.marginTop,
            width - paddingRight,
            offsetY + flexboxEmoji.marginTop + flexboxEmoji.measuredHeight
        )
    }

    private enum class MessageType {
        ME_MESSAGE, CHAT_MESSAGE
    }

    private fun setChatMessage(
        message: UiMessage.ChatMessage,
        onReactionClick: ((message: UiMessage, emoji: Emoji) -> Unit)? = null
    ) {
        ivAvatar.loadFromUrl(message.senderAvatarUrl)
        tvSender.text = message.senderName
        tvContent.text = message.content
        ivAvatar.isVisible = true
        tvSender.isVisible = true
        addReactions(message.reactions, message, onReactionClick)
        messageType = MessageType.CHAT_MESSAGE
    }

    private fun setUserMessage(
        message: UiMessage.MeMessage,
        onReactionClick: ((message: UiMessage, emoji: Emoji) -> Unit)? = null
    ) {
        ivAvatar.isVisible = false
        tvSender.isVisible = false
        tvContent.text = message.content
        addReactions(message.reactions, message, onReactionClick)
        messageType = MessageType.ME_MESSAGE
    }

    fun addReactions(
        reactions: List<UiReaction>,
        message: UiMessage,
        onReactionClick: ((message: UiMessage, emoji: Emoji) -> Unit)? = null
    ) {
        flexboxEmoji.removeAllViews()
        reactions.toMessageEmojiViews(context, message, onReactionClick).forEach {
            flexboxEmoji.addView(it)
        }
    }

}
