package com.study.channels.presentation.addChannel

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.study.channels.databinding.FragmentAddChannelBinding
import com.study.channels.di.ChannelsComponentViewModel
import com.study.channels.presentation.addChannel.elm.AddChannelEffect
import com.study.channels.presentation.addChannel.elm.AddChannelEvent
import com.study.channels.presentation.addChannel.elm.AddChannelState
import com.study.channels.presentation.channels.util.toErrorMessage
import com.study.common.extension.fastLazy
import com.study.components.model.UiError
import vivid.money.elmslie.android.screen.ElmDelegate
import vivid.money.elmslie.android.screen.ElmScreen
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject
import com.study.ui.R as CoreR

internal class AddChannelFragment : BottomSheetDialogFragment(),
    ElmDelegate<AddChannelEvent, AddChannelEffect, AddChannelState> {
    private var _binding: FragmentAddChannelBinding? = null
    private val binding get() = _binding!!
    override val initEvent: AddChannelEvent = AddChannelEvent.Ui.Init

    @Inject
    lateinit var store: Store<AddChannelEvent, AddChannelEffect, AddChannelState>

    override val storeHolder: StoreHolder<AddChannelEvent, AddChannelEffect, AddChannelState> by fastLazy {
        LifecycleAwareStoreHolder(lifecycle) { store }
    }

    override fun onAttach(context: Context) {
        ViewModelProvider(this).get<ChannelsComponentViewModel>().channelsComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ElmScreen(this, lifecycle) { requireActivity() }
        setStyle(STYLE_NORMAL, CoreR.style.BaseBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddChannelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        with(binding) {
            fragmentAddChannelTvError.isVisible = false
            fragmentAddChannelInputView.btnSubmitClickListener = { channelTitle ->
                store.accept(
                    AddChannelEvent.Ui.AddChannel(
                        channelTitle,
                        fragmentAddChannelCbHistory.isChecked
                    )
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun render(state: AddChannelState) {
        when {
            state.isLoading -> binding.fragmentAddChannelTvError.isVisible = false
            state.error != null -> showError(state.error.toErrorMessage())
            state.successfullyAdded -> dismiss()
        }
    }

    private fun showError(error: UiError) {
        binding.fragmentAddChannelTvError.run {
            isVisible = true
            text = if (error.descriptionRes != null && error.descriptionArgs != null) {
                getString(error.descriptionRes!!, error.descriptionArgs)
            } else {
                getString(error.messageRes)
            }
        }
    }
}
