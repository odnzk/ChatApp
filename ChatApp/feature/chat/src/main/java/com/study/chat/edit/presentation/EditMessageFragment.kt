package com.study.chat.edit.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.navArgs
import com.study.chat.R
import com.study.chat.databinding.FragmentEditMessageBinding
import com.study.chat.edit.presentation.elm.EditMessageEffect
import com.study.chat.edit.presentation.elm.EditMessageEvent
import com.study.chat.edit.presentation.elm.EditMessageState
import com.study.chat.common.di.ChatComponentViewModel
import com.study.chat.common.presentation.util.setupSuggestionsAdapter
import com.study.chat.common.presentation.util.toErrorMessage
import com.study.components.ext.showToast
import com.study.components.customview.BaseBottomSheetFragment
import vivid.money.elmslie.android.storeholder.StoreHolder
import javax.inject.Inject

internal class EditMessageFragment :
    BaseBottomSheetFragment<EditMessageEvent, EditMessageEffect, EditMessageState>() {
    private var _binding: FragmentEditMessageBinding? = null
    private val binding: FragmentEditMessageBinding get() = _binding!!
    private val args: EditMessageFragmentArgs by navArgs()
    override val initEvent: EditMessageEvent get() = EditMessageEvent.Ui.Init(args.messageId)
    override val storeHolder: StoreHolder<EditMessageEvent, EditMessageEffect, EditMessageState> get() = editStoreHolder

    @Inject
    lateinit var editStoreHolder: StoreHolder<EditMessageEvent, EditMessageEffect, EditMessageState>

    override fun onAttach(context: Context) {
        ViewModelProvider(this).get<ChatComponentViewModel>().chatComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun render(state: EditMessageState) {
        if (state.message != null) {
            initMessage(state.message.topic, state.message.content)
        }
        if (state.topicsSuggestions.isNotEmpty()) {
            binding.fragmentEditEtTopic.setupSuggestionsAdapter(state.topicsSuggestions)
        }
    }

    override fun handleEffect(effect: EditMessageEffect) = when (effect) {
        is EditMessageEffect.ShowError -> showToast(effect.error.toErrorMessage().messageRes)
        EditMessageEffect.Success -> {
            showToast(R.string.success_update_message)
            dismiss()
        }
        is EditMessageEffect.ShowSynchronizationError -> {
            showToast(effect.error.toErrorMessage().messageRes)
            dismiss()
        }
    }

    private fun initUI() = with(binding) {
        fragmentEditBtnDone.isEnabled = false
        fragmentEditBtnDone.setOnClickListener {
            storeHolder.store.accept(
                EditMessageEvent.Ui.UpdateMessage(
                    topic = fragmentEditEtTopic.text.toString(),
                    content = fragmentEditEtContent.text.toString()
                )
            )
        }
    }

    private fun initMessage(topic: String, content: String) = with(binding) {
        fragmentEditEtTopic.setText(topic)
        fragmentEditEtContent.setText(content)
        fragmentEditBtnDone.isEnabled = true
    }
}
