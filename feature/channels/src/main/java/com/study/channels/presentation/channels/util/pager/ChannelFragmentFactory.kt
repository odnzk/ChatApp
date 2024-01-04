package com.study.channels.presentation.channels.util.pager

import androidx.fragment.app.Fragment
import com.study.channels.presentation.channels.ChannelsFragment
import com.study.channels.presentation.channels.model.UiChannelFilter

internal class ChannelFragmentFactory : FragmentPagerFactory {
    override fun getFragmentCount(): Int = 2
    override fun getFragmentCreator(position: Int): () -> Fragment = when (position) {
        SUBSCRIBED_CHANNELS_POSITION -> {
            { ChannelsFragment.newInstance(UiChannelFilter.SUBSCRIBED_ONLY) }
        }

        ALL_CHANNELS_POSITION -> {
            { ChannelsFragment.newInstance(UiChannelFilter.ALL) }
        }

        else -> {
            error("Cannot create fragment for this position")
        }
    }

    fun getFilterForPosition(position: Int): UiChannelFilter? {
        return when (position) {
            SUBSCRIBED_CHANNELS_POSITION -> UiChannelFilter.SUBSCRIBED_ONLY
            ALL_CHANNELS_POSITION -> UiChannelFilter.ALL
            else -> null
        }
    }

    companion object {
        private const val SUBSCRIBED_CHANNELS_POSITION = 0
        private const val ALL_CHANNELS_POSITION = 1
    }
}
