package com.study.components.recycler.shimmer

import android.view.ViewGroup
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.facebook.shimmer.ShimmerFrameLayout
import com.study.ui.R

abstract class ShimmerItemViewHolder<T : Any>(private val binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    protected open val shimmerBackgroundColor: Int =
        itemView.context.getColor(R.color.bottom_nav_background_color)
    protected open val transparentColor: Int =
        itemView.context.getColor((android.R.color.transparent))

    protected abstract fun showContent(data: T)

    fun bind(shimmerItem: ShimmerItem<*>) {
        val content = shimmerItem.content()
        if (content != null) {
            (binding.root as? ShimmerFrameLayout)?.run {
                manageBackground(isShimmering = false)
                hideShimmer()
            }
                ?: error("Binding root view must be ShimmerFrameLayout")
            (content as? T)?.let {
                showContent(content)
            } ?: error("ShimmerItem type must match with ViewHolder type")
        } else {
            (binding.root as? ShimmerFrameLayout)?.run {
                manageBackground(isShimmering = true)
                startShimmer()
            } ?: error("Binding root view must be ShimmerFrameLayout")
        }
    }

    private fun ViewGroup.manageBackground(isShimmering: Boolean) {
        val background = if (isShimmering) shimmerBackgroundColor else transparentColor
        children.filterIsInstance<ViewGroup>().forEach { viewGroup ->
            for (child in viewGroup.children) {
                child.setBackgroundColor(background)
            }
        }
    }
}

