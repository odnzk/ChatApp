package com.study.tinkoff.feature.channels.presentation

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
import com.study.common.extensions.safeGetParcelable
import com.study.common.rv.delegates.Delegate
import com.study.common.rv.delegates.GeneralAdapterDelegate
import com.study.components.BaseScreenStateFragment
import com.study.components.view.ScreenStateView
import com.study.tinkoff.databinding.FragmentRecyclerViewBinding
import com.study.tinkoff.feature.channels.presentation.delegates.channel.ChannelDelegate
import com.study.tinkoff.feature.channels.presentation.delegates.topic.ChannelTopicDelegate
import com.study.tinkoff.feature.channels.presentation.model.UiChannelModel
import kotlinx.parcelize.Parcelize

class ChannelsFragment :
    BaseScreenStateFragment<ChannelsViewModel, FragmentRecyclerViewBinding, List<UiChannelModel>>() {
    private var _binding: FragmentRecyclerViewBinding? = null
    override val binding: FragmentRecyclerViewBinding get() = _binding!!
    override val viewModel: ChannelsViewModel by viewModels()
    override val screenStateView: ScreenStateView get() = binding.fragmentScreenStateView
    private var channelsAdapter: GeneralAdapterDelegate? = null

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

    override fun onError(error: Throwable) = Unit

    override fun onLoading() = Unit

    override fun onSuccess(data: List<UiChannelModel>) {
        channelsAdapter?.submitList(data)
    }

    override fun observeState() {
        viewModel.state.collectScreenStateSafely()
    }

    override fun setupListeners() = Unit

    override fun initUI() {
        channelsAdapter =
            GeneralAdapterDelegate(listOf(ChannelDelegate(onChannelClick = { channelId ->
                viewModel.onEvent(ChannelsEvent.UpdateChannelTopics(channelId))
            }), ChannelTopicDelegate(onTopicClick = { channelId, lastMessageId ->
                findNavController().navigate(
                    HolderChannelsFragmentDirections.actionHolderChannelsFragmentToChatFragment(
                        channelId, lastMessageId
                    )
                )
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
        private const val CHANNEL_ID_KEY = "channel id"
        private const val LAST_MES_ID_KEY = "last message id"
        private const val CHANNEL_SETTING_KEY = "channel settings"
        fun newInstance(settings: ChannelsSettings): ChannelsFragment {
            return ChannelsFragment().apply {
                arguments = bundleOf(CHANNEL_SETTING_KEY to settings)
            }
        }
    }
}
