package com.study.chat.actions.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.study.chat.actions.presentation.elm.ActionsEffect
import com.study.chat.actions.presentation.elm.ActionsEvent
import com.study.chat.actions.presentation.elm.ActionsState
import com.study.chat.actions.presentation.model.UiAction
import com.study.chat.actions.presentation.model.UiUserRole
import com.study.chat.actions.presentation.util.delegate.ActionDelegate
import com.study.chat.databinding.FragmentBottomSheetListBinding
import com.study.chat.shared.di.ChatComponentViewModel
import com.study.chat.shared.presentation.util.navigateToEditMessageFragment
import com.study.chat.shared.presentation.util.navigateToEmojiListFragment
import com.study.chat.shared.presentation.util.toErrorMessage
import com.study.components.extension.delegatesToList
import com.study.components.extension.showToast
import com.study.components.recycler.delegates.GeneralAdapterDelegate
import com.study.components.view.BaseBottomSheetFragment
import vivid.money.elmslie.android.storeholder.StoreHolder
import javax.inject.Inject

internal class ActionsFragment :
    BaseBottomSheetFragment<ActionsEvent, ActionsEffect, ActionsState>() {
    private var _binding: FragmentBottomSheetListBinding? = null
    private val binding get() = _binding!!
    private var adapter: GeneralAdapterDelegate? = null
    private val args: ActionsFragmentArgs by navArgs()
    override val initEvent: ActionsEvent get() = ActionsEvent.Ui.Init(args.messageId)
    override val storeHolder: StoreHolder<ActionsEvent, ActionsEffect, ActionsState> get() = actionsStoreHolder

    @Inject
    lateinit var actionsStoreHolder: StoreHolder<ActionsEvent, ActionsEffect, ActionsState>

    override fun onAttach(context: Context) {
        ViewModelProvider(this).get<ChatComponentViewModel>().chatComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
    }


    override fun render(state: ActionsState) {
        when {
            state.isLoading -> binding.bottomSheetPbLoading.show()
            state.userRole != UiUserRole.UNDEFINED -> {
                binding.bottomSheetPbLoading.hide()
                adapter?.submitList(state.userRole.allowedActions)
            }
        }
    }

    override fun handleEffect(effect: ActionsEffect) {
        when (effect) {
            is ActionsEffect.ShowError -> {
                binding.bottomSheetPbLoading.hide()
                showToast(effect.error.toErrorMessage().messageRes)
            }
            is ActionsEffect.SuccessAction -> {
                binding.bottomSheetPbLoading.hide()
                showToast(effect.action.successTextRes)
                if (effect.action == UiAction.DELETE) dismiss()
            }
            is ActionsEffect.ShowSynchronizationError -> {
                showToast(effect.error.toErrorMessage().messageRes)
                dismiss()
            }
        }
    }

    private fun initUI() {
        adapter = GeneralAdapterDelegate(delegatesToList(ActionDelegate(::onActionClickListener)))
        binding.bottomSheetRvList.run {
            adapter = this@ActionsFragment.adapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
        storeHolder.store
    }

    private fun onActionClickListener(action: UiAction) = when (action) {
        UiAction.ADD_REACTION -> navigateToEmojiListFragment(args.resultKey)
        UiAction.COPY -> storeHolder.store.accept(ActionsEvent.Ui.CopyMessage(args.messageId))
        UiAction.DELETE -> storeHolder.store.accept(ActionsEvent.Ui.DeleteMessage(args.messageId))
        UiAction.EDIT -> navigateToEditMessageFragment(args.messageId)
    }

}
