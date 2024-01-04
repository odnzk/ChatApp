package com.study.channels.presentation.channels

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.study.channels.R
import com.study.channels.presentation.channels.model.SearchEvent
import com.study.channels.presentation.channels.model.UiChannelFilter
import com.study.channels.presentation.channels.util.pager.ChannelFragmentFactory
import com.study.channels.presentation.channels.util.pager.PagerAdapter
import com.study.channels.di.ChannelsComponentViewModel
import com.study.channels.di.annotation.SearchFlow
import com.study.channels.presentation.navigateToAddChannel
import com.study.channels.databinding.FragmentChannelsHolderBinding
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

internal class HolderChannelsFragment : Fragment() {

    @Inject
    @SearchFlow
    lateinit var searchFlow: MutableSharedFlow<SearchEvent>

    private var _binding: FragmentChannelsHolderBinding? = null
    private val binding: FragmentChannelsHolderBinding get() = _binding!!
    private var pagerAdapter: PagerAdapter? = null
    private var pagerMediator: TabLayoutMediator? = null
    private var selectedFilter: UiChannelFilter? = null

    override fun onAttach(context: Context) {
        ViewModelProvider(requireActivity()).get<ChannelsComponentViewModel>().channelsComponent.inject(
            this
        )
        super.onAttach(context)
    }

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
            fragmentChannelsHolderVp.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    selectedFilter = pagerAdapter?.getFilterForPosition(position)
                }
            })
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
                    selectedFilter?.let { filter ->
                        searchFlow.tryEmit(SearchEvent(query.orEmpty(), filter))
                    }
                    return false
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    selectedFilter?.let { filter ->
                        searchFlow.tryEmit(SearchEvent(query.orEmpty(), filter))
                    }
                    return false
                }
            })
        }
    }
}
