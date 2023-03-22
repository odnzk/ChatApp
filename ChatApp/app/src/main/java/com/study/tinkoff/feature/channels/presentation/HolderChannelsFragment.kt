package com.study.tinkoff.feature.channels.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.study.tinkoff.databinding.FragmentChannelsBinding
import com.study.ui.R

class HolderChannelsFragment : Fragment() {
    private var _binding: FragmentChannelsBinding? = null
    private val binding: FragmentChannelsBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChannelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPagerWithTabs()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViewPagerWithTabs() {
        val tabs: List<String> = listOf(
            getString(R.string.fragment_channels_tab_subscribed_channels),
            getString(R.string.fragment_channels_tab_all_channels)
        )
        val fragments = listOf<Fragment>(
            ChannelsFragment.newInstance(ChannelsFragment.ChannelsSettings.SUBSCRIBED_ONLY),
            ChannelsFragment.newInstance(ChannelsFragment.ChannelsSettings.ALL)
        )
        val pagerAdapter = PagerAdapter(parentFragmentManager, lifecycle, fragments)
        with(binding) {
            fragmentChannelsHolderViewPagerChannels.adapter = pagerAdapter
            TabLayoutMediator(
                fragmentChannelsHolderTabLayoutChannels, fragmentChannelsHolderViewPagerChannels
            ) { tab, position ->
                tab.text = tabs[position]
            }.attach()
        }
    }
}
