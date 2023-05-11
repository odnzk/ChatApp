package com.study.chat.presentation.edit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.study.chat.R
import com.study.chat.databinding.FragmentEditMessageBinding
import com.study.chat.di.ChatComponentViewModel
import com.study.chat.presentation.edit.elm.EditMessageEffect
import com.study.chat.presentation.edit.elm.EditMessageEvent
import com.study.chat.presentation.edit.elm.EditMessageState
import com.study.chat.presentation.util.setupSuggestionsAdapter
import com.study.chat.presentation.util.toErrorMessage
import com.study.common.extension.fastLazy
import com.study.components.BaseBottomSheetFragment
import com.study.components.extension.showErrorSnackbar
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

internal class EditMessageFragment :
    BaseBottomSheetFragment<EditMessageEvent, EditMessageEffect, EditMessageState>() {
    private var _binding: FragmentEditMessageBinding? = null
    private val binding: FragmentEditMessageBinding get() = _binding!!
    private val args: EditMessageFragmentArgs by navArgs()

    @Inject
    lateinit var store: Store<EditMessageEvent, EditMessageEffect, EditMessageState>

    override val initEvent: EditMessageEvent get() = EditMessageEvent.Ui.Init(args.messageId)
    override val storeHolder: StoreHolder<EditMessageEvent, EditMessageEffect, EditMessageState> by fastLazy {
        LifecycleAwareStoreHolder(lifecycle) { store }
    }

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
        is EditMessageEffect.ShowError -> showErrorSnackbar(
            binding.root, effect.error, Throwable::toErrorMessage
        ) { updateMessage() }
        EditMessageEffect.Success -> {
            Snackbar.make(binding.root, R.string.success_update_message, Snackbar.LENGTH_SHORT)
                .show()
            dismiss()
        }
    }

    private fun initUI() = with(binding) {
        fragmentEditBtnDone.isEnabled = false
        fragmentEditBtnDone.setOnClickListener { updateMessage() }
    }

    private fun updateMessage() = with(binding) {
        store.accept(
            EditMessageEvent.Ui.UpdateMessage(
                topic = fragmentEditEtTopic.text.toString(),
                content = fragmentEditEtContent.text.toString()
            )
        )
    }

    private fun initMessage(topic: String, content: String) = with(binding) {
        fragmentEditEtTopic.setText(topic)
        fragmentEditEtContent.setText(content)
        fragmentEditBtnDone.isEnabled = true
    }
}
