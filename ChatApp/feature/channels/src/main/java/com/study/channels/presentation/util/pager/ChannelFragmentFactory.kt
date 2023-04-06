package com.study.channels.presentation.util.pager

import androidx.fragment.app.Fragment
import com.study.channels.presentation.ChannelsFragment

internal class ChannelFragmentFactory : FragmentPagerFactory {
    override fun getFragmentCount(): Int = 2

    override fun getFragmentCreator(position: Int): () -> Fragment = when (position) {
        0 -> {
            { ChannelsFragment.newInstance(ChannelsFragment.ChannelsSettings.SUBSCRIBED_ONLY) }
        }
        else -> {
            { ChannelsFragment.newInstance(ChannelsFragment.ChannelsSettings.ALL) }
        }
    }

}
