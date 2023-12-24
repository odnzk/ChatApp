package com.study.chat.emoji.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.navArgs
import com.study.chat.databinding.FragmentBottomSheetListBinding
import com.study.chat.emoji.presentation.elm.EmojiListEffect
import com.study.chat.emoji.presentation.elm.EmojiListEvent
import com.study.chat.emoji.presentation.elm.EmojiListState
import com.study.chat.emoji.presentation.util.EmojiDelegate
import com.study.chat.common.di.ChatComponentViewModel
import com.study.chat.common.presentation.util.toErrorMessage
import com.study.components.ext.delegatesToList
import com.study.components.ext.dp
import com.study.components.ext.showErrorSnackbar
import com.study.components.recycler.delegates.GeneralAdapterDelegate
import com.study.components.recycler.VarSpanGridLayoutManager
import com.study.components.customview.BaseBottomSheetFragment
import vivid.money.elmslie.android.storeholder.StoreHolder
import javax.inject.Inject

internal class EmojiListFragment :
    BaseBottomSheetFragment<EmojiListEvent, EmojiListEffect, EmojiListState>() {
    private var _binding: FragmentBottomSheetListBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<EmojiListFragmentArgs>()
    private var mainEmojiAdapter: GeneralAdapterDelegate? = null
    override val initEvent: EmojiListEvent = EmojiListEvent.Ui.Init
    override val storeHolder: StoreHolder<EmojiListEvent, EmojiListEffect, EmojiListState> get() = emojiStoreHolder

    @Inject
    lateinit var emojiStoreHolder: StoreHolder<EmojiListEvent, EmojiListEffect, EmojiListState>

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
        mainEmojiAdapter = null
        _binding = null
    }

    override fun render(state: EmojiListState) {
        when {
            state.isLoading -> binding.bottomSheetPbLoading.show()
            state.error != null -> with(binding) {
                bottomSheetPbLoading.hide()
                showErrorSnackbar(state.error, Throwable::toErrorMessage) {
                    storeHolder.store.accept(EmojiListEvent.Ui.Reload)
                }
            }
            state.emojis.isNotEmpty() -> {
                binding.bottomSheetPbLoading.hide()
                mainEmojiAdapter?.submitList(state.emojis)
            }
        }
    }

    private fun initUI() {
        mainEmojiAdapter = GeneralAdapterDelegate(
            delegatesToList(
                EmojiDelegate(onEmojiClick = { emoji ->
                    setFragmentResult(args.resultKey, bundleOf(args.resultKey to emoji))
                    dismiss()
                }
                )
            )
        )
        with(binding.bottomSheetRvList) {
            adapter = mainEmojiAdapter
            layoutManager = VarSpanGridLayoutManager(context, 80f.dp(context), MIN_GRID_COLUMNS)
        }
    }

    companion object {
        private const val MIN_GRID_COLUMNS = 7
    }
}
