package com.study.channels.presentation.util.delegates.channel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.study.channels.databinding.ItemChannelBinding
import com.study.channels.presentation.util.model.UiChannel
import com.study.components.recycler.shimmer.ShimmerItem
import com.study.components.recycler.shimmer.ShimmerItemViewHolder
import com.study.ui.R as CoreR

internal class ChannelViewHolder(
    private val binding: ItemChannelBinding,
    private val onChannelClick: ((channelId: Int, isCollapsed: Boolean) -> Unit)?
) : ShimmerItemViewHolder<UiChannel>(binding) {

    override fun showContent(data: UiChannel) {
        with(binding) {
            setCollapsedStatusIcon(data.isCollapsed)
            itemChannelTvChannelName.text = data.title
            root.setOnClickListener { onChannelClick?.invoke(data.id, data.isCollapsed) }
        }
    }

    fun bindWithPayloads(channel: ShimmerItem<UiChannel>, payloads: Any) {
        channel.content()?.let { channelContent ->
            if ((payloads as Bundle).getBoolean(PAYLOAD_IS_COLLAPSED_CHANGED)) {
                setCollapsedStatusIcon(channelContent.isCollapsed)
            }
            binding.root.setOnClickListener {
                onChannelClick?.invoke(channelContent.id, channelContent.isCollapsed)
            }
        }
    }

    private fun setCollapsedStatusIcon(isCollapsed: Boolean) {
        val icon = if (isCollapsed) {
            CoreR.drawable.ic_baseline_hide_24
        } else {
            CoreR.drawable.ic_baseline_more_24
        }
        binding.itemChannelIvBtnShowTopics.setImageResource(icon)
    }


    companion object {
        internal const val PAYLOAD_IS_COLLAPSED_CHANGED = "isCollapsed"
        fun create(
            parent: ViewGroup, onChannelClick: ((channelId: Int, isCollapsed: Boolean) -> Unit)?
        ): ChannelViewHolder = ChannelViewHolder(
            ItemChannelBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onChannelClick = onChannelClick
        )
    }
}
