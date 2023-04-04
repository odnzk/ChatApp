package com.study.channels.presentation.util.holder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.study.channels.databinding.FragmentChannelsBinding
import com.study.channels.presentation.util.pager.ChannelFragmentFactory
import com.study.channels.presentation.util.pager.PagerAdapter
import com.study.ui.R

internal class HolderChannelsFragment : Fragment() {
    private var _binding: FragmentChannelsBinding? = null
    private val binding: FragmentChannelsBinding get() = _binding!!
    private val holderViewModel: HolderChannelViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChannelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPagerWithTabs()
        setupSearchView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupSearchView() {
        with(binding.fragmentChannelsSearchView) {
            afterChangeTextListener = { query ->
                query?.let {
                    holderViewModel.search(it.toString())
                }
            }
        }
    }

    private fun setupViewPagerWithTabs() {
        val tabs: List<String> = listOf(
            getString(R.string.fragment_channels_tab_subscribed_channels),
            getString(R.string.fragment_channels_tab_all_channels)
        )
        val pagerAdapter = PagerAdapter(childFragmentManager, lifecycle, ChannelFragmentFactory())
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
