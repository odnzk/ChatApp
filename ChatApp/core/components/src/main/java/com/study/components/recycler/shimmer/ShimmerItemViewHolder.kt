package com.study.components.recycler.shimmer

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.facebook.shimmer.ShimmerFrameLayout
import com.study.ui.R

/**
 * An abstract class for [RecyclerView.ViewHolder] that can display shimmer layouts.
 *
 * @param [T] The type of data this [RecyclerView.ViewHolder] displays.
 * @property [binding] The ViewBinding for this ViewHolder's layout.
 * @property [shimmerBackgroundColor] The color of the background for shimmer frames.
 * @property [transparentColor] The color for hiding shimmers background.
 */
abstract class ShimmerItemViewHolder<T : Any>(private val binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    protected open val shimmerBackgroundColor: Int =
        ContextCompat.getColor(itemView.context, R.color.common_shimmer_background_color)

    protected open val transparentColor: Int =
        ContextCompat.getColor(itemView.context, android.R.color.transparent)

    /**
     * Displays the content represented by the data object.
     *
     * @param data The data object to show in this ViewHolder.
     */
    protected abstract fun showContent(data: T)

    /**
     * Binds a [ShimmerItem] to the [RecyclerView.ViewHolder].
     *
     * @param shimmerItem The [ShimmerItem] for binding to the [RecyclerView.ViewHolder] .
     * @throws IllegalStateException if the binding root view is not a [ShimmerFrameLayout].
     */
    fun bind(shimmerItem: ShimmerItem<*>) {
        val content = shimmerItem.content()
        if (content != null) {
            (binding.root as? ShimmerFrameLayout)?.run {
                manageBackground(isShimmering = false)
                hideShimmer()
            } ?: error("Binding root view must be ShimmerFrameLayout")
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
            viewGroup.children.forEach { child -> child.setBackgroundColor(background) }
        }
    }
}

