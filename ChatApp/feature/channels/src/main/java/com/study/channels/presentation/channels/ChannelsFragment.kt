package com.study.channels.presentation.channels

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.study.channels.databinding.FragmentChannelsBinding
import com.study.channels.di.ChannelsComponentViewModel
import com.study.channels.presentation.channels.elm.ChannelsEffect
import com.study.channels.presentation.channels.elm.ChannelsEvent
import com.study.channels.presentation.channels.elm.ChannelsState
import com.study.channels.presentation.channels.util.delegate.channel.ChannelDelegate
import com.study.channels.presentation.channels.util.delegate.topic.ChannelTopicDelegate
import com.study.channels.presentation.channels.util.mapper.toChannelsList
import com.study.channels.presentation.channels.util.model.UiChannelFilter
import com.study.channels.presentation.channels.util.model.UiChannelShimmer
import com.study.channels.presentation.channels.util.navigateToAddChannel
import com.study.channels.presentation.channels.util.navigateToChatFragment
import com.study.channels.presentation.util.toErrorMessage
import com.study.common.extension.fastLazy
import com.study.common.search.NothingFoundForThisQueryException
import com.study.components.extension.collectFlowSafely
import com.study.components.extension.createStoreHolder
import com.study.components.extension.delegatesToList
import com.study.components.extension.safeGetParcelable
import com.study.components.extension.showErrorSnackbar
import com.study.components.recycler.delegates.GeneralAdapterDelegate
import com.study.components.view.ScreenStateView.ViewState
import kotlinx.coroutines.flow.Flow
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

internal class ChannelsFragment : ElmFragment<ChannelsEvent, ChannelsEffect, ChannelsState>() {
    private val binding: FragmentChannelsBinding get() = _binding!!
    private var _binding: FragmentChannelsBinding? = null
    private var channelsAdapter: GeneralAdapterDelegate? = null
    private val channelFilter
        get() = arguments?.safeGetParcelable<UiChannelFilter>(CHANNEL_SETTING_KEY)
            ?: error("Invalid channel filter ")

    @Inject
    lateinit var searchFlow: Flow<String>

    @Inject
    lateinit var channelsStore: Store<ChannelsEvent, ChannelsEffect, ChannelsState>
    override val initEvent: ChannelsEvent get() = ChannelsEvent.Ui.Init(channelFilter)

    override val storeHolder: StoreHolder<ChannelsEvent, ChannelsEffect, ChannelsState> by fastLazy {
        createStoreHolder(channelsStore)
    }

    override fun onAttach(context: Context) {
        ViewModelProvider(this).get<ChannelsComponentViewModel>().channelsComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChannelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        setupListeners()
        observeSearchQuery()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        channelsAdapter = null
        _binding = null
    }

    override fun render(state: ChannelsState) {
        when {
            state.error != null -> {
                val showBtnTryAgain = state.error !is NothingFoundForThisQueryException
                channelsAdapter?.submitList(emptyList())
                binding.screenStateView.setState(
                    ViewState.Error(
                        state.error.toErrorMessage(),
                        showBtnTryAgain
                    )
                )
            }
            state.isLoading && state.channelsWithTopics.isEmpty() -> {
                binding.screenStateView.setState(ViewState.Success)
                val shimmers = List(UiChannelShimmer.DEFAULT_SHIMMER_COUNT) { UiChannelShimmer }
                channelsAdapter?.submitList(shimmers)
            }
            state.isLoading -> binding.screenStateView.setState(ViewState.Loading)
            state.channelsWithTopics.isNotEmpty() -> {
                binding.screenStateView.setState(ViewState.Success)
                channelsAdapter?.submitList(state.channelsWithTopics.toChannelsList())
            }
        }
    }

    override fun handleEffect(effect: ChannelsEffect) {
        when (effect) {
            is ChannelsEffect.ShowWarning -> {
                showErrorSnackbar(binding.root, effect.error, Throwable::toErrorMessage)
            }
        }
    }

    private fun initUI() {
        channelsAdapter = GeneralAdapterDelegate(
            delegatesToList(
                ChannelDelegate(
                    onChannelClick = ::onChannelClick,
                    onChannelLongClick = ::navigateToChatFragment
                ),
                ChannelTopicDelegate(onTopicClick = ::navigateToChatFragment)
            )
        )
        with(binding) {
            screenStateView.setState(ViewState.Success)
            fragmentRvDataList.run {
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                layoutManager = LinearLayoutManager(context)
                adapter = channelsAdapter
            }
        }
    }

    private fun onChannelClick(channelId: Int, isCollapsed: Boolean) {
        store.accept(
            ChannelsEvent.Ui.ManageChannelTopics(channelId, isCollapsed)
        )
    }

    private fun setupListeners() {
        with(binding) {
            fragmentChannelsBtnAddChannel.setOnClickListener {
                navigateToAddChannel()
            }
            screenStateView.onTryAgainClickListener = View.OnClickListener {
                store.accept(ChannelsEvent.Ui.Reload)
            }
        }
    }

    private fun observeSearchQuery() {
        collectFlowSafely(searchFlow, Lifecycle.State.RESUMED) {
            store.accept(ChannelsEvent.Ui.Search(it))
        }
    }


    companion object {
        private const val CHANNEL_SETTING_KEY = "channel settings"
        fun newInstance(settings: UiChannelFilter): ChannelsFragment {
            return ChannelsFragment().apply {
                arguments = bundleOf(CHANNEL_SETTING_KEY to settings)
            }
        }
    }
}
