package com.study.components.recycler.shimmer

/**
 * A generic interface for items that can be displayed using a shimmer layout.
 *
 * @param [T] The type of content this item represents.
 */
interface ShimmerItem<T : Any> {

    /**
     * Returns the content this item represents, or null if the content is not yet available.
     *
     * @return content of item, or null if the content is not yet available.
     */
    fun content(): T?
}

