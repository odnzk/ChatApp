package com.study.channels.channels.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.study.channels.R
import com.study.channels.channels.presentation.util.pager.ChannelFragmentFactory
import com.study.channels.channels.presentation.util.pager.PagerAdapter
import com.study.channels.common.presentation.navigateToAddChannel
import com.study.channels.databinding.FragmentChannelsHolderBinding

internal class HolderChannelsFragment : Fragment() {
    private var _binding: FragmentChannelsHolderBinding? = null
    private val binding: FragmentChannelsHolderBinding get() = _binding!!
    private var pagerAdapter: PagerAdapter? = null
    private var pagerMediator: TabLayoutMediator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChannelsHolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        pagerAdapter = null
        pagerMediator?.detach()
        _binding = null
    }

    private fun initUI() {
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
            fragmentChannelsHolderVp.adapter = pagerAdapter
            pagerMediator = TabLayoutMediator(
                fragmentChannelsHolderTb, fragmentChannelsHolderVp
            ) { tab, position ->
                tab.text = tabs[position]
            }
            pagerMediator?.attach()
            fragmentChannelsBtnAddChannel.setOnClickListener { navigateToAddChannel() }
            fragmentChannelsHolderSearchView.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    //TODO("implement search")
//                    query?.let { store.accept(MainEvent.Ui.Search(query)) }
                    return false
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    //  TODO("implement search")
//                    query?.let { store.accept(MainEvent.Ui.Search(query)) }
                    return false
                }
            })
        }
    }
}
