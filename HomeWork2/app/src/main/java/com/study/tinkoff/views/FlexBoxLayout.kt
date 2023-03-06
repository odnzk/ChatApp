package com.study.tinkoff.views

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.content.withStyledAttributes
import androidx.core.view.children
import androidx.core.view.isVisible
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import com.study.tinkoff.R
import com.study.tinkoff.common.extensions.dp
import java.lang.Integer.max

class FlexBoxLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    var onPlusClickListener: () -> Unit = {}
        set(value) {
            field = value
            ivPlus.setOnClickListener { field() }
        }

    private var lineSpacing = DEFAULT_LINE_SPACING
    private var itemSpacing = DEFAULT_ITEM_SPACING
    private val ivPlus = ShapeableImageView(context).apply {
        imageTintList = ColorStateList.valueOf(context.getColor(R.color.white))
        setBackgroundColor(context.getColor(R.color.item_background_color))
        setImageResource(R.drawable.ic_round_add)
        shapeAppearanceModel = ShapeAppearanceModel().withCornerSize(ICON_PLUS_CORNERS)
        // post because of bug (contentPadding not working when set via Kotlin),
        // prove: https://github.com/material-components/material-components-android/issues/2063)
        post {
            setContentPadding(
                ICON_PLUS_HORIZONTAL_PADDING.dp(context),
                ICON_PLUS_VERTICAL_PADDING.dp(context),
                ICON_PLUS_HORIZONTAL_PADDING.dp(context),
                ICON_PLUS_VERTICAL_PADDING.dp(context)
            )
        }
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.FlexBoxLayout) {
            lineSpacing =
                getInt(R.styleable.FlexBoxLayout_line_spacing, DEFAULT_LINE_SPACING).dp(context)
            itemSpacing =
                getInt(R.styleable.FlexBoxLayout_item_spacing, DEFAULT_ITEM_SPACING).dp(context)

        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val maxWidth =
            if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.EXACTLY) MeasureSpec.getSize(
                widthMeasureSpec
            ) else Int.MAX_VALUE
        if (childCount < 1) {
            setMeasuredDimension(maxWidth, heightMeasureSpec)
            return
        }
        if (children.filter { it.isVisible }.count() > 0 && ivPlus.parent == null) {
            this@FlexBoxLayout.addView(ivPlus)
        }
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        val maxEnd = maxWidth - paddingEnd
        var childStart = paddingStart
        var childTop = paddingTop
        var childEnd = childStart
        var childBottom = childTop
        var totalWidth = 0
        children.forEach { child ->
            val newChildEnd = childEnd + child.measuredWidth + itemSpacing
            if (newChildEnd > maxEnd) {
                childStart = paddingStart
                childTop = childBottom + lineSpacing
            } else {
                childStart = childEnd + itemSpacing
            }
            childEnd = childStart + child.measuredWidth
            childBottom = max(childTop + child.measuredHeight, childBottom)
            totalWidth = max(totalWidth, childEnd)
        }
        totalWidth = resolveSize(totalWidth, widthMeasureSpec)
        setMeasuredDimension(totalWidth, childBottom + paddingBottom)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount < 1) {
            return
        }
        val firstChild = getChildAt(0)
        var childStart = paddingStart
        var childTop = paddingTop
        var childEnd = childStart + firstChild.measuredWidth
        var childBottom = childTop + firstChild.measuredHeight
        val maxEnd = width - paddingEnd
        firstChild.layout(childStart, childTop, childEnd, childBottom)

        for (child in children.drop(1)) {
            if (!child.isVisible) {
                continue
            }
            val newChildEnd = childEnd + child.measuredWidth
            if (newChildEnd > maxEnd) {
                childStart = paddingStart
                childTop = childBottom + lineSpacing
                childBottom = childTop + child.measuredHeight
            } else {
                childStart = childEnd + itemSpacing
                childBottom = max(child.measuredHeight, childBottom)
            }
            childEnd = childStart + child.measuredWidth
            child.layout(childStart, childTop, childEnd, childBottom)
        }
    }

    companion object {
        private const val DEFAULT_LINE_SPACING = 4
        private const val DEFAULT_ITEM_SPACING = 2
        private const val ICON_PLUS_CORNERS = 32f
        private const val ICON_PLUS_HORIZONTAL_PADDING = 8f
        private const val ICON_PLUS_VERTICAL_PADDING = 4f
    }

}
