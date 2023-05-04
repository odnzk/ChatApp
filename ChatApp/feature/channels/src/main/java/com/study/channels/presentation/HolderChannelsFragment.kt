package com.study.channels.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.study.channels.databinding.FragmentChannelsBinding
import com.study.channels.presentation.util.pager.ChannelFragmentFactory
import com.study.components.util.PagerAdapter
import com.study.ui.R

internal class HolderChannelsFragment : Fragment() {
    private var _binding: FragmentChannelsBinding? = null
    private val binding: FragmentChannelsBinding get() = _binding!!
    private var pagerAdapter: PagerAdapter? = null
    private var pagerMediator: TabLayoutMediator? = null

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
        pagerAdapter = null
        pagerMediator?.detach()
        _binding = null
    }

    private fun setupViewPagerWithTabs() {
        val tabs: List<String> = listOf(
            getString(R.string.fragment_channels_tab_subscribed_channels),
            getString(R.string.fragment_channels_tab_all_channels)
        )
        pagerAdapter = PagerAdapter(
            childFragmentManager,
            viewLifecycleOwner.lifecycle,
            ChannelFragmentFactory()
        )
        with(binding) {
            fragmentChannelsVp.adapter = pagerAdapter
            pagerMediator = TabLayoutMediator(
                fragmentChannelsTabLayout, fragmentChannelsVp
            ) { tab, position ->
                tab.text = tabs[position]
            }
            pagerMediator?.attach()
        }
    }
}
