package com.study.channels.channels.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.study.channels.channels.presentation.elm.ChannelsActor
import com.study.channels.channels.presentation.elm.ChannelsEffect
import com.study.channels.channels.presentation.elm.ChannelsEvent
import com.study.channels.channels.presentation.elm.ChannelsReducer
import com.study.channels.channels.presentation.elm.ChannelsState
import com.study.channels.channels.presentation.model.UiChannelFilter
import com.study.channels.channels.presentation.model.UiChannelShimmer
import com.study.channels.channels.presentation.util.delegate.channel.ChannelDelegate
import com.study.channels.channels.presentation.util.delegate.topic.ChannelTopicDelegate
import com.study.channels.channels.presentation.util.mapper.toChannelsList
import com.study.channels.common.di.ChannelsComponentViewModel
import com.study.channels.common.presentation.navigateToChatFragment
import com.study.channels.common.presentation.toErrorMessage
import com.study.channels.databinding.FragmentChannelsBinding
import com.study.common.ext.fastLazy
import com.study.common.search.NothingFoundForThisQueryException
import com.study.components.customview.ScreenStateView.ViewState
import com.study.components.ext.delegatesToList
import com.study.components.ext.safeGetParcelable
import com.study.components.ext.showErrorSnackbar
import com.study.components.recycler.delegates.GeneralAdapterDelegate
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.coroutines.ElmStoreCompat
import javax.inject.Inject

internal class ChannelsFragment : ElmFragment<ChannelsEvent, ChannelsEffect, ChannelsState>() {

    @Inject
    lateinit var reducer: ChannelsReducer

    @Inject
    lateinit var actorFactory: ChannelsActor.Factory

    private val binding: FragmentChannelsBinding get() = _binding!!
    private var _binding: FragmentChannelsBinding? = null
    private var channelsAdapter: GeneralAdapterDelegate? = null
    private val channelFilter
        get() = arguments?.safeGetParcelable<UiChannelFilter>(CHANNEL_SETTING_KEY)
            ?: error("Invalid channel filter ")

    override val initEvent: ChannelsEvent get() = ChannelsEvent.Ui.Init

    override val storeHolder: StoreHolder<ChannelsEvent, ChannelsEffect, ChannelsState> by fastLazy {
        LifecycleAwareStoreHolder(lifecycle) {
            ElmStoreCompat(
                ChannelsState(),
                reducer,
                actorFactory.create(channelFilter)
            )
        }
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
                channelsAdapter?.submitList(emptyList())
                binding.screenStateView.setState(
                    ViewState.Error(
                        state.error.toErrorMessage(),
                        state.error !is NothingFoundForThisQueryException
                    )
                )
            }

            state.isLoading && state.channelsWithTopics.isEmpty() -> {
                binding.screenStateView.setState(ViewState.Success)
                val shimmers = List(UiChannelShimmer.DEFAULT_SHIMMER_COUNT) { UiChannelShimmer }
                channelsAdapter?.submitList(shimmers)
            }

            state.isLoading -> binding.screenStateView.setState(ViewState.Loading)
            else -> {
                binding.screenStateView.setState(ViewState.Success)
                channelsAdapter?.submitList(state.channelsWithTopics.toChannelsList())
            }
        }
    }

    override fun handleEffect(effect: ChannelsEffect) = when (effect) {
        is ChannelsEffect.ShowWarning -> binding.showErrorSnackbar(
            effect.error,
            Throwable::toErrorMessage
        )

        is ChannelsEffect.ShowSynchronizationError -> binding.showErrorSnackbar(
            effect.error,
            Throwable::toErrorMessage
        ) {
            store.accept(ChannelsEvent.Ui.Reload)
        }
    }


    private fun initUI() {
        channelsAdapter = GeneralAdapterDelegate(
            delegatesToList(
                ChannelDelegate(
                    onChannelClick = { channelId, isCollapsed ->
                        store.accept(ChannelsEvent.Ui.ManageChannelTopics(channelId, isCollapsed))
                    },
                    onChannelLongClick = ::navigateToChatFragment
                ),
                ChannelTopicDelegate(onTopicClick = { topic ->
                    navigateToChatFragment(
                        topic.channelId,
                        channelTitle = topic.channelTitle,
                        topicTitle = topic.title,
                        topicColor = topic.backgroundColor
                    )
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

    private fun observeSearchQuery() {
        // TODO("implement search")
//        collectFlowSafely(searchFlow, Lifecycle.State.RESUMED) {
//            store.accept(ChannelsEvent.Ui.Search(it))
//        }
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
