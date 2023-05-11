package com.study.chat.presentation.actions

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.study.chat.R
import com.study.chat.databinding.FragmentBottomSheetListBinding
import com.study.chat.di.ChatComponentViewModel
import com.study.chat.presentation.actions.elm.ActionsEffect
import com.study.chat.presentation.actions.elm.ActionsEvent
import com.study.chat.presentation.actions.elm.ActionsState
import com.study.chat.presentation.actions.util.delegate.ActionDelegate
import com.study.chat.presentation.actions.util.model.UiAction
import com.study.chat.presentation.actions.util.model.UiUserRole
import com.study.chat.presentation.util.navigateToEditMessageFragment
import com.study.chat.presentation.util.navigateToEmojiListFragment
import com.study.common.extension.fastLazy
import com.study.components.BaseBottomSheetFragment
import com.study.components.extension.delegatesToList
import com.study.components.extension.showErrorSnackbar
import com.study.components.recycler.delegates.GeneralAdapterDelegate
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

internal class ActionsFragment :
    BaseBottomSheetFragment<ActionsEvent, ActionsEffect, ActionsState>() {
    private var _binding: FragmentBottomSheetListBinding? = null
    private val binding get() = _binding!!
    private var adapter: GeneralAdapterDelegate? = null

    private val args: ActionsFragmentArgs by navArgs()
    override val initEvent: ActionsEvent get() = ActionsEvent.Ui.Init(args.messageId)

    @Inject
    lateinit var store: Store<ActionsEvent, ActionsEffect, ActionsState>

    override val storeHolder: StoreHolder<ActionsEvent, ActionsEffect, ActionsState> by fastLazy {
        LifecycleAwareStoreHolder(lifecycle) { store }
    }

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
            state.userRole != UiUserRole.UNDEFINED -> {
                adapter?.submitList(state.userRole.allowedActions)
            }
        }
    }

    override fun handleEffect(effect: ActionsEffect) {
        when (effect) {
            is ActionsEffect.ShowError -> showErrorSnackbar(binding.root, effect.error)
            is ActionsEffect.SuccessAction -> {
                Toast.makeText(context, effect.action.successTextRes, Toast.LENGTH_SHORT).show()
                if (effect.action == UiAction.DELETE) {
                    dismiss()
                }
            }
            ActionsEffect.ShowSynchronizationError -> {
                Toast.makeText(
                    context,
                    R.string.error_message_is_not_synchronized,
                    Toast.LENGTH_SHORT
                ).show()
                dismiss()
            }
        }
    }

    private fun initUI() {
        adapter = GeneralAdapterDelegate(delegatesToList(ActionDelegate(::onActionClickListener)))
        binding.fragmentEmojiListRvEmoji.run {
            adapter = this@ActionsFragment.adapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun onActionClickListener(action: UiAction) = when (action) {
        UiAction.ADD_REACTION -> navigateToEmojiListFragment(args.resultKey)
        UiAction.COPY -> store.accept(ActionsEvent.Ui.CopyMessage(args.messageId))
        UiAction.DELETE -> store.accept(ActionsEvent.Ui.DeleteMessage(args.messageId))
        UiAction.EDIT -> navigateToEditMessageFragment(args.messageId)
    }

}
