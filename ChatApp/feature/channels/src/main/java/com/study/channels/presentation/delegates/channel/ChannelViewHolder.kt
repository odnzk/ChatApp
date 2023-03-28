package com.study.channels.presentation.delegates.channel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.study.channels.databinding.ItemChannelBinding
import com.study.channels.presentation.model.UiChannel
import com.study.ui.R as CoreResources

internal class ChannelViewHolder(
    private val binding: ItemChannelBinding, private val onChannelClick: ((channelId: Int) -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(channel: UiChannel) {
        with(binding) {
            val icRes =
                if (channel.isCollapsed) {
                    CoreResources.drawable.ic_baseline_hide_24
                } else CoreResources.drawable.ic_baseline_more_24
            itemChannelIvBtnShowTopics.setImageResource(icRes)
            root.setOnClickListener {
                onChannelClick?.invoke(channel.id)
            }
            itemChannelTvChannelName.text = channel.title
        }
    }

    companion object {
        fun create(
            parent: ViewGroup, onChannelClick: ((channelId: Int) -> Unit)?
        ): ChannelViewHolder = ChannelViewHolder(
            ItemChannelBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onChannelClick = onChannelClick
        )
    }
}
