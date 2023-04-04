package com.study.components.recycler.delegates

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView

class GeneralPaginationAdapterDelegate(private val delegates: List<Delegate<RecyclerView.ViewHolder, Any>>) :
    PagingDataAdapter<Any, RecyclerView.ViewHolder>(GeneralItemCallback(delegates)) {

    override fun getItemViewType(position: Int): Int {
        val currentItem = getItem(position)
        return currentItem?.let { delegates.indexOfFirst { delegate -> delegate.isType(currentItem) } }
            ?: LOADING_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == LOADING_TYPE) return LoadingViewHolder.create(parent)
        return delegates[viewType].viewHolderCreator(parent)
    }

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
