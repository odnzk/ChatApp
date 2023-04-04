package com.study.channels.presentation

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.study.channels.presentation.model.UiChannelModel
import com.study.channels.presentation.model.UiChannelShimmer
import com.study.channels.presentation.util.delegates.channel.ChannelDelegate
import com.study.channels.presentation.util.delegates.topic.ChannelTopicDelegate
import com.study.channels.presentation.util.holder.HolderChannelViewModel
import com.study.channels.presentation.util.navigation.navigateToChannelTopic
import com.study.components.BaseScreenStateFragment
import com.study.components.databinding.FragmentRecyclerViewBinding
import com.study.components.extensions.safeGetParcelable
import com.study.components.recycler.delegates.Delegate
import com.study.components.recycler.delegates.GeneralAdapterDelegate
import kotlinx.parcelize.Parcelize

internal class ChannelsFragment :
    BaseScreenStateFragment<ChannelsViewModel, FragmentRecyclerViewBinding, List<UiChannelModel>>() {
    override val binding: FragmentRecyclerViewBinding get() = _binding!!
    override val viewModel: ChannelsViewModel by viewModels()
    override val onTryAgainClick: View.OnClickListener
        get() = View.OnClickListener { viewModel.onEvent(ChannelFragmentEvent.Reload) }
    private var _binding: FragmentRecyclerViewBinding? = null
    private var channelsAdapter: GeneralAdapterDelegate? = null
    private val holderViewModel: HolderChannelViewModel by viewModels({ requireParentFragment() })

    @Parcelize
    enum class ChannelsSettings : Parcelable {
        ALL, SUBSCRIBED_ONLY
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecyclerViewBinding.inflate(inflater, container, false)
        arguments?.safeGetParcelable<ChannelsSettings>(CHANNEL_SETTING_KEY)?.let { settings ->
            viewModel.updateChannelSettings(settings)
        }
        screenStateView = binding.fragmentScreenStateView
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        screenStateView = null
        channelsAdapter = null
        _binding = null
    }

    override fun onLoading() {
        val shimmers: List<UiChannelModel> =
            List(UiChannelShimmer.DEFAULT_SHIMMER_COUNT) { UiChannelShimmer }
        channelsAdapter?.submitList(shimmers)
    }

    override fun onSuccess(data: List<UiChannelModel>) {
        channelsAdapter?.submitList(data)
    }

    override fun observeState() {
        viewModel.state.collectScreenStateSafely()
        holderViewModel.state.collectFlowSafely { query ->
            viewModel.onEvent(ChannelFragmentEvent.Search(query))
        }
    }

    override fun initUI() {
        channelsAdapter =
            GeneralAdapterDelegate(listOf(ChannelDelegate(onChannelClick = { channelId ->
                viewModel.onEvent(ChannelFragmentEvent.UpdateChannelTopics(channelId))
            }), ChannelTopicDelegate(onTopicClick = { channelTitle, topicTitle ->
                navigateToChannelTopic(channelTitle = channelTitle, topicTitle = topicTitle)
            })
            ) as List<Delegate<RecyclerView.ViewHolder, Any>>
            )
        binding.fragmentRvDataList.run {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(context)
            adapter = channelsAdapter
        }
    }

    companion object {
        private const val CHANNEL_SETTING_KEY = "channel settings"
        fun newInstance(settings: ChannelsSettings): ChannelsFragment {
            return ChannelsFragment().apply {
                arguments = bundleOf(CHANNEL_SETTING_KEY to settings)
            }
        }
    }
}
