package com.study.components.recycler.delegates

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * A generic pagination adapter delegate to support binding data to a variety of view types with pagination support.
 *
 * @param delegates a list of [Delegate] instances to bind data to the target views.
 */
class GeneralPaginationAdapterDelegate(private val delegates: List<Delegate<RecyclerView.ViewHolder, Any>>) :
    PagingDataAdapter<Any, RecyclerView.ViewHolder>(GeneralItemCallback(delegates)) {

    /**
     * Returns the view type that corresponds to the data at the specified [position].
     * If the item corresponding to [position] is null, returns a [LOADING_TYPE].
     */
    override fun getItemViewType(position: Int): Int {
        val currentItem = getItem(position)
        return currentItem?.let { delegates.indexOfFirst { delegate -> delegate.isType(currentItem) } }
            ?: LOADING_TYPE
    }

    /**
     * Creates a new [RecyclerView.ViewHolder] instance for the item specified by [viewType].
     * If the [viewType] is a [LOADING_TYPE], returns a [LoadingViewHolder].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == LOADING_TYPE) return LoadingViewHolder.create(parent)
        return delegates[viewType].viewHolderCreator(parent)
    }

    /**
     * Binds the data at the specified [position] to the specified [holder].
     * If the item is a loading binds nothing.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            if (getItemViewType(position) == LOADING_TYPE) return
            delegates[getItemViewType(position)].viewBinder(holder, item)
        }
    }

    companion object {
        private const val LOADING_TYPE = -1
    }
}
