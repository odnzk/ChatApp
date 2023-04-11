package com.study.channels.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.study.channels.domain.exceptions.toErrorMessage
import com.study.channels.domain.model.ChannelFilter
import com.study.channels.domain.repository.ChannelRepository
import com.study.channels.domain.usecase.GetChannelTopicsUseCase
import com.study.channels.domain.usecase.GetChannelsUseCase
import com.study.channels.domain.usecase.SearchChannelUseCase
import com.study.channels.presentation.elm.ChannelsEffect
import com.study.channels.presentation.elm.ChannelsEvent
import com.study.channels.presentation.elm.ChannelsState
import com.study.channels.presentation.elm.ChannelsStoreFactory
import com.study.channels.presentation.util.delegates.channel.ChannelDelegate
import com.study.channels.presentation.util.delegates.topic.ChannelTopicDelegate
import com.study.channels.presentation.util.mapper.toChannelsList
import com.study.channels.presentation.util.model.UiChannelModel
import com.study.channels.presentation.util.model.UiChannelShimmer
import com.study.channels.presentation.util.navigation.navigateToChannelTopic
import com.study.common.extensions.fastLazy
import com.study.components.databinding.FragmentRecyclerViewBinding
import com.study.components.extensions.createStoreHolder
import com.study.components.extensions.delegatesToList
import com.study.components.extensions.safeGetParcelable
import com.study.components.extensions.showErrorSnackbar
import com.study.components.recycler.delegates.GeneralAdapterDelegate
import com.study.components.view.ScreenStateView.ViewState
import com.study.network.NetworkModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.StoreHolder

internal class ChannelsFragment :
    ElmFragment<ChannelsEvent, ChannelsEffect, ChannelsState>() {
    private val binding: FragmentRecyclerViewBinding get() = _binding!!
    private var _binding: FragmentRecyclerViewBinding? = null
    private var channelsAdapter: GeneralAdapterDelegate? = null
    private val channelFilter
        get() = arguments?.safeGetParcelable<ChannelFilter>(CHANNEL_SETTING_KEY)
            ?: error("Invalid channel filter ")

    override val initEvent: ChannelsEvent = ChannelsEvent.Ui.Init
    override val storeHolder: StoreHolder<ChannelsEvent, ChannelsEffect, ChannelsState> by fastLazy {
        val repository = ChannelRepository(NetworkModule.providesApi())
        val dispatcher = Dispatchers.Default
        createStoreHolder(
            ChannelsStoreFactory(
                channelFilter,
                GetChannelsUseCase(repository, dispatcher),
                GetChannelTopicsUseCase(repository, dispatcher),
                SearchChannelUseCase(repository, dispatcher)
            ).create()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecyclerViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        setupListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        channelsAdapter = null
        _binding = null
    }

    override fun render(state: ChannelsState) {
        lifecycleScope.launch {
            when {
                state.isLoading && state.channelsWithTopics.isEmpty() -> {
                    with(binding) {
                        screenStateView.setState(ViewState.Success)
                        fragmentRvDataList.isVisible = false
                    }
                    val shimmers: List<UiChannelModel> =
                        List(UiChannelShimmer.DEFAULT_SHIMMER_COUNT) { UiChannelShimmer }
                    channelsAdapter?.submitList(shimmers)
                }
                state.channelsWithTopics.isNotEmpty() -> {
                    with(binding) {
                        screenStateView.setState(ViewState.Success)
                        fragmentRvDataList.isVisible = true
                    }
                    channelsAdapter?.submitList(state.channelsWithTopics.toChannelsList())
                }
            }
        }
    }

    override fun handleEffect(effect: ChannelsEffect) {
        when (effect) {
            is ChannelsEffect.ShowWarning -> {
                showErrorSnackbar(binding.root, effect.error, Throwable::toErrorMessage)
            }
            is ChannelsEffect.ShowError -> with(binding) {
                screenStateView.setState(ViewState.Error(effect.error.toErrorMessage()))
                fragmentRvDataList.isVisible = false
            }
        }
    }

    private fun initUI() {
        channelsAdapter =
            GeneralAdapterDelegate(
                delegatesToList(
                    ChannelDelegate(onChannelClick = { channelId ->
                        store.accept(ChannelsEvent.Ui.ManageChannelTopics(channelId))
                    }),
                    ChannelTopicDelegate(onTopicClick = { channelTitle, topicTitle ->
                        navigateToChannelTopic(channelTitle = channelTitle, topicTitle = topicTitle)
                    })
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

    private fun setupListeners() {
        with(binding) {
            screenStateView.onTryAgainClickListener = View.OnClickListener {
                store.accept(ChannelsEvent.Ui.Reload)
            }
        }
    }


    companion object {
        private const val CHANNEL_SETTING_KEY = "channel settings"
        fun newInstance(settings: ChannelFilter): ChannelsFragment {
            return ChannelsFragment().apply {
                arguments = bundleOf(CHANNEL_SETTING_KEY to settings)
            }
        }
    }
}
