package com.study.channels.presentation.addChannel

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.study.channels.presentation.addChannel.elm.AddChannelEffect
import com.study.channels.presentation.addChannel.elm.AddChannelEvent
import com.study.channels.presentation.addChannel.elm.AddChannelState
import com.study.channels.databinding.FragmentAddChannelBinding
import com.study.channels.di.ChannelsComponentViewModel
import com.study.channels.presentation.toErrorMessage
import com.study.components.model.UiError
import com.study.components.customview.BaseBottomSheetFragment
import vivid.money.elmslie.android.storeholder.StoreHolder
import javax.inject.Inject

internal class AddChannelFragment :
    BaseBottomSheetFragment<AddChannelEvent, AddChannelEffect, AddChannelState>() {
    private var _binding: FragmentAddChannelBinding? = null
    private val binding get() = _binding!!
    override val initEvent: AddChannelEvent = AddChannelEvent.Ui.Init
    override val storeHolder: StoreHolder<AddChannelEvent, AddChannelEffect, AddChannelState>
        get() = addChannelStoreHolder

    @Inject
    lateinit var addChannelStoreHolder: StoreHolder<AddChannelEvent, AddChannelEffect, AddChannelState>

    override fun onAttach(context: Context) {
        ViewModelProvider(this).get<ChannelsComponentViewModel>().channelsComponent.inject(this)
        super.onAttach(context)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun render(state: AddChannelState) {
        when {
            state.isLoading -> with(binding) {
                fragmentAddChannelPbLoading.show()
                fragmentAddChannelTvError.isVisible = false
            }
            state.error != null -> {
                binding.fragmentAddChannelPbLoading.hide()
                showError(state.error.toErrorMessage())
            }
            state.successfullyAdded -> dismiss()
        }
    }

    private fun showError(error: UiError) {
        binding.fragmentAddChannelTvError.run {
            isVisible = true
            text = error.getDescription(context) ?: error.getMessage(context)
        }
    }

    private fun initUI() {
        with(binding) {
            fragmentAddChannelTvError.isVisible = false
            fragmentAddChannelPbLoading.hide()
            fragmentAddChannelBtnAddChannel.setOnClickListener {
                val channelTitle = fragmentAddEtChannel.text?.toString() ?: return@setOnClickListener
                storeHolder.store.accept(AddChannelEvent.Ui.AddChannel(channelTitle))
            }
        }
    }
}
