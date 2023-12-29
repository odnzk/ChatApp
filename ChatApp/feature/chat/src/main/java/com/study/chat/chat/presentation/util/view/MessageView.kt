package com.study.chat.chat.presentation.util.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.os.Build
import android.util.AttributeSet
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import coil.load
import com.google.android.material.color.MaterialColors
import com.study.chat.R
import com.study.chat.chat.presentation.model.UiEmoji
import com.study.chat.chat.presentation.model.UiMessage
import com.study.chat.chat.presentation.model.UiReaction
import com.study.chat.chat.presentation.util.mapper.toMessageEmojiViews
import com.study.common.ext.maxOfThee
import com.study.components.ext.dp
import com.study.components.ext.measureFullHeight
import com.study.components.ext.measureFullWidth
import com.study.components.ext.measureHeightIfVisible
import com.study.components.ext.measureWidthIfVisible
import com.study.components.customview.FlexBoxLayout
import com.study.network.util.toUserUploadsUrl
import java.lang.Integer.max
import com.google.android.material.R as MaterialR
import com.study.ui.R as CoreResources


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
internal class MessageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    private val ivAvatar: ImageView
    private val tvSender: TextView
    private val tvContent: TextView
    private val ivContent: ImageView
    private val flexboxEmoji: FlexBoxLayout
    private val textBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val colorDark = MaterialColors.getColor(
        context,
        MaterialR.attr.backgroundColor,
        ContextCompat.getColor(context, CoreResources.color.dark_nero)
    )
    private val colorAccent = MaterialColors.getColor(
        context,
        androidx.appcompat.R.attr.colorPrimary,
        context.getColor(CoreResources.color.purple_light)
    )
    private val colorDarkAccent = context.getColor(com.study.ui.R.color.purple_dark)
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
        ivContent = findViewById(R.id.view_message_iv_content)
        flexboxEmoji = findViewById(R.id.view_message_fl_emoji)
    }


    fun setMessage(
        message: UiMessage,
        onReactionClick: ((message: UiMessage, emoji: UiEmoji) -> Unit)? = null
    ) {
        when (message) {
            is UiMessage.ChatMessage -> setChatMessage(message, onReactionClick)
            is UiMessage.MeMessage -> setUserMessage(message, onReactionClick)
        }
    }

    fun addReactions(
        reactions: List<UiReaction>,
        message: UiMessage,
        onReactionClick: ((message: UiMessage, emoji: UiEmoji) -> Unit)? = null
    ) {
        flexboxEmoji.removeAllViews()
        reactions.toMessageEmojiViews(context, message, onReactionClick).forEach {
            flexboxEmoji.addView(it)
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val allowedWidthSpec = MeasureSpec.makeMeasureSpec(
            (MeasureSpec.getSize(widthMeasureSpec) * maxWidthPercent).toInt(),
            MeasureSpec.getMode(widthMeasureSpec)
        )
        val (resWidth, resHeight) = when (messageType) {
            MessageType.ME_MESSAGE -> {
                measureMeMessage(allowedWidthSpec, widthMeasureSpec, heightMeasureSpec)
            }

            MessageType.CHAT_MESSAGE -> measureChatMessage(
                allowedWidthSpec, widthMeasureSpec, heightMeasureSpec
            )
        }
        measureMessageBackground(resWidth)
        setMeasuredDimension(resWidth, resHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        when (messageType) {
            MessageType.ME_MESSAGE -> layoutMeMessage()
            MessageType.CHAT_MESSAGE -> layoutChatMessage()
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.drawRoundRect(
            textBackground,
            textBackgroundCornerRadius,
            textBackgroundCornerRadius,
            textBackgroundPaint
        )
        super.dispatchDraw(canvas)
    }

    override fun generateDefaultLayoutParams(): LayoutParams =
        MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

    override fun generateLayoutParams(p: LayoutParams): LayoutParams = MarginLayoutParams(p)
    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams =
        MarginLayoutParams(context, attrs)

    override fun checkLayoutParams(p: LayoutParams): Boolean = p is MarginLayoutParams

    private enum class MessageType {
        ME_MESSAGE, CHAT_MESSAGE
    }

    private fun measureMeMessage(
        allowedWidthSpec: Int, widthSpec: Int, heightSpec: Int
    ): Pair<Int, Int> {
        var heightUsed = measureMessageContent(allowedWidthSpec, 0, heightSpec, 0)
        measureChildWithMargins(
            flexboxEmoji,
            allowedWidthSpec,
            0,
            heightSpec,
            heightUsed
        )
        heightUsed += measureFullHeight(flexboxEmoji)
        val totalWidth = paddingLeft + paddingRight + maxOfThee(
            measureFullWidth(tvContent), measureFullWidth(flexboxEmoji), measureFullWidth(ivContent)
        )
        val totalHeight = paddingTop + paddingBottom + heightUsed
        return resolveSize(totalWidth, widthSpec) to resolveSize(totalHeight, heightSpec)
    }

    private fun measureChatMessage(
        allowedWidthSpec: Int, widthSpec: Int, heightSpec: Int
    ): Pair<Int, Int> {
        var heightUsed = 0
        measureChildWithMargins(ivAvatar, allowedWidthSpec, 0, heightSpec, 0)
        val avatarWidth = measureFullWidth(ivAvatar)
        measureChildWithMargins(tvSender, allowedWidthSpec, avatarWidth, heightSpec, 0)
        heightUsed += measureFullHeight(tvSender)
        heightUsed += measureMessageContent(allowedWidthSpec, avatarWidth, heightSpec, heightUsed)
        measureChildWithMargins(flexboxEmoji, allowedWidthSpec, avatarWidth, heightSpec, heightUsed)
        heightUsed += measureFullHeight(flexboxEmoji)
        val maxContentWidth = maxOfThee(
            measureFullWidth(tvSender),
            measureFullWidth(tvContent),
            measureFullWidth(ivContent)
        )
        val totalWidth = paddingLeft + paddingRight + avatarWidth + max(
            maxContentWidth,
            flexboxEmoji.measuredWidth
        )
        val totalHeight = paddingTop + paddingBottom + max(measureFullHeight(ivAvatar), heightUsed)
        return resolveSize(totalWidth, widthSpec) to resolveSize(totalHeight, heightSpec)
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
        offsetX += measureFullWidth(ivAvatar)
        tvSender.layout(
            offsetX + tvSender.marginLeft,
            offsetY + tvSender.marginTop,
            offsetX + tvSender.measuredWidth + tvSender.marginLeft,
            offsetY + tvSender.marginTop + tvSender.measuredHeight
        )
        offsetY += measureFullHeight(tvSender)
        if (tvContent.isVisible) {
            tvContent.layout(
                offsetX + tvContent.marginLeft,
                offsetY + tvContent.marginTop,
                offsetX + tvContent.measuredWidth + tvContent.marginLeft,
                offsetY + tvContent.marginTop + tvContent.measuredHeight
            )
            offsetY += measureFullHeight(tvContent)
        }
        if (ivContent.isVisible) {
            ivContent.layout(
                offsetX + ivContent.marginLeft,
                offsetY + ivContent.marginTop,
                offsetX + ivContent.measuredWidth + ivContent.marginLeft,
                offsetY + ivContent.measuredHeight + ivContent.marginTop
            )
            offsetY += measureFullHeight(ivContent)
        }

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
        val rightBorder = width - paddingRight
        if (tvContent.isVisible) {
            tvContent.layout(
                offsetX - tvContent.measuredWidth - tvContent.marginRight,
                offsetY + tvContent.marginTop,
                rightBorder - tvContent.marginRight,
                offsetY + tvContent.marginTop + tvContent.measuredHeight
            )
            offsetY += measureFullHeight(tvContent)
        }
        if (ivContent.isVisible) {
            ivContent.layout(
                offsetX - ivContent.measuredWidth - ivContent.marginRight,
                offsetY + ivContent.marginTop,
                rightBorder - ivContent.marginRight,
                offsetY + ivContent.marginTop + ivContent.measuredHeight
            )
            offsetY += measureFullHeight(ivContent)
        }
        flexboxEmoji.layout(
            offsetX - flexboxEmoji.measuredWidth - flexboxEmoji.marginRight,
            offsetY + flexboxEmoji.marginTop,
            rightBorder - flexboxEmoji.marginRight,
            offsetY + flexboxEmoji.marginTop + flexboxEmoji.measuredHeight
        )
    }


    private fun setChatMessage(
        message: UiMessage.ChatMessage,
        onReactionClick: ((message: UiMessage, emoji: UiEmoji) -> Unit)? = null
    ) {
        ivAvatar.isVisible = true
        tvSender.isVisible = true
        ivAvatar.load(message.senderAvatarUrl)
        tvSender.text = message.senderName
        setMessageContent(message.content, message.imageUrl)
        addReactions(message.reactions, message, onReactionClick)
        messageType = MessageType.CHAT_MESSAGE
    }

    private fun setUserMessage(
        message: UiMessage.MeMessage,
        onReactionClick: ((message: UiMessage, emoji: UiEmoji) -> Unit)? = null
    ) {
        ivAvatar.isVisible = false
        tvSender.isVisible = false
        setMessageContent(message.content, message.imageUrl)
        addReactions(message.reactions, message, onReactionClick)
        messageType = MessageType.ME_MESSAGE
    }

    private fun setMessageContent(content: String, imageUrl: String?) {
        tvContent.isVisible = if (content.isNotBlank()) {
            tvContent.text = content
            true
        } else false
        ivContent.isVisible = if (imageUrl != null) {
            ivContent.load(toUserUploadsUrl(imageUrl))
            true
        } else false
    }


    private fun measureMessageContent(
        parentWidthSpec: Int,
        widthUsed: Int,
        parentHeightSpec: Int,
        heightUsed: Int
    ): Int {
        var contentHeight = 0
        if (tvContent.isVisible) {
            measureChildWithMargins(
                tvContent,
                parentWidthSpec,
                widthUsed,
                parentHeightSpec,
                heightUsed
            )
            contentHeight += measureFullHeight(tvContent)
        }
        if (ivContent.isVisible) {
            measureChildWithMargins(
                ivContent,
                parentWidthSpec,
                widthUsed,
                parentHeightSpec,
                heightUsed + contentHeight
            )
            contentHeight += measureFullHeight(ivContent)
        }
        return contentHeight
    }

    private fun measureMessageBackground(totalWidth: Int) {
        val contentWidth = max(measureWidthIfVisible(tvContent), measureWidthIfVisible(ivContent))
        val contentHeight = measureHeightIfVisible(tvContent) + measureHeightIfVisible(ivContent)
        when (messageType) {
            MessageType.ME_MESSAGE -> {
                val offsetX = (totalWidth - paddingRight - contentWidth).toFloat()
                textBackground.set(
                    offsetX,
                    paddingTop.toFloat(),
                    totalWidth.toFloat() - paddingRight,
                    (paddingTop + contentHeight).toFloat()
                )
                textBackgroundPaint.shader = LinearGradient(
                    offsetX,
                    paddingTop.toFloat(),
                    totalWidth.toFloat() - paddingRight,
                    (paddingTop + contentWidth).toFloat(),
                    colorAccent,
                    colorDarkAccent,
                    Shader.TileMode.MIRROR
                )
            }

            MessageType.CHAT_MESSAGE -> {
                val offsetX = (paddingLeft + measureFullWidth(ivAvatar)).toFloat()
                val chatContentWidth = max(measureFullWidth(tvSender), contentWidth)
                val chatContentHeight =
                    (paddingTop + measureFullHeight(tvSender) + contentHeight + paddingBottom).toFloat()
                textBackground.set(
                    offsetX,
                    paddingTop.toFloat(),
                    offsetX + chatContentWidth,
                    chatContentHeight
                )
                with(textBackgroundPaint) {
                    shader = null
                    color = colorDark
                }
            }
        }
    }
}
