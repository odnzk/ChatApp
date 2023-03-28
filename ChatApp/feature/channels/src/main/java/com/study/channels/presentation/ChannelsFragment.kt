package com.study.channels.presentation

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.study.channels.presentation.delegates.channel.ChannelDelegate
import com.study.channels.presentation.delegates.topic.ChannelTopicDelegate
import com.study.channels.presentation.holder.HolderChannelViewModel
import com.study.channels.presentation.model.UiChannelModel
import com.study.channels.presentation.navigation.toChannelTopic
import com.study.common.extensions.safeGetParcelable
import com.study.common.rv.delegates.Delegate
import com.study.common.rv.delegates.GeneralAdapterDelegate
import com.study.components.BaseScreenStateFragment
import com.study.components.databinding.FragmentRecyclerViewBinding
import com.study.components.view.ScreenStateView
import kotlinx.parcelize.Parcelize

internal class ChannelsFragment :
    BaseScreenStateFragment<ChannelsViewModel, FragmentRecyclerViewBinding, List<UiChannelModel>>() {
    override val binding: FragmentRecyclerViewBinding get() = _binding!!
    override val viewModel: ChannelsViewModel by viewModels()
    override val screenStateView: ScreenStateView get() = binding.fragmentScreenStateView
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
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        channelsAdapter = null
        _binding = null
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

    override fun setupListeners() = Unit

    override fun initUI() {
        channelsAdapter =
            GeneralAdapterDelegate(listOf(ChannelDelegate(onChannelClick = { channelId ->
                viewModel.onEvent(ChannelFragmentEvent.UpdateChannelTopics(channelId))
            }), ChannelTopicDelegate(onTopicClick = { channelId, lastMessageId ->
                findNavController().toChannelTopic(channelId, lastMessageId)
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
