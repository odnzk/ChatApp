package com.study.chat.presentation.emoji

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.study.chat.domain.exceptions.toErrorMessage
import com.study.chat.domain.repository.EmojiRepository
import com.study.chat.presentation.emoji.delegates.EmojiDelegate
import com.study.chat.presentation.emoji.elm.EmojiListEffect
import com.study.chat.presentation.emoji.elm.EmojiListEvent
import com.study.chat.presentation.emoji.elm.EmojiListFactory
import com.study.chat.presentation.emoji.elm.EmojiListState
import com.study.common.extensions.fastLazy
import com.study.components.extensions.createStoreHolder
import com.study.components.extensions.delegatesToList
import com.study.components.extensions.dp
import com.study.components.extensions.showErrorSnackbar
import com.study.components.recycler.delegates.GeneralAdapterDelegate
import com.study.components.recycler.manager.VarSpanGridLayoutManager
import com.study.feature.R
import com.study.feature.databinding.FragmentEmojiListBinding
import vivid.money.elmslie.android.screen.ElmDelegate
import vivid.money.elmslie.android.screen.ElmScreen
import vivid.money.elmslie.android.storeholder.StoreHolder

internal class EmojiListFragment : BottomSheetDialogFragment(),
    ElmDelegate<EmojiListEvent, EmojiListEffect, EmojiListState> {
    private var _binding: FragmentEmojiListBinding? = null
    private val binding get() = _binding!!
    private var mainEmojiAdapter: GeneralAdapterDelegate? = null
    private val args by navArgs<EmojiListFragmentArgs>()

    override val initEvent: EmojiListEvent = EmojiListEvent.Ui.Init
    override val storeHolder: StoreHolder<EmojiListEvent, EmojiListEffect, EmojiListState> by fastLazy {
        createStoreHolder(lifecycle, EmojiListFactory(EmojiRepository()).create())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ElmScreen(this, lifecycle) { requireActivity() }
        setStyle(STYLE_NORMAL, R.style.ChatBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmojiListBinding.inflate(inflater, container, false)
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
            state.error != null -> {
                showErrorSnackbar(binding.root, state.error, Throwable::toErrorMessage)
            }
            state.emojis.isNotEmpty() -> {
                mainEmojiAdapter?.submitList(state.emojis)
            }
        }
    }

    private fun initUI() {
        mainEmojiAdapter = GeneralAdapterDelegate(
            delegatesToList(
                EmojiDelegate(onEmojiClick = { emoji ->
                    setFragmentResult(
                        args.resultKey,
                        bundleOf(args.resultKey to emoji)
                    )
                    dismiss()
                }
                )
            )
        )
        with(binding) {
            fragmentEmojiListRvEmoji.run {
                adapter = mainEmojiAdapter
                layoutManager = VarSpanGridLayoutManager(
                    context,
                    80f.dp(context),
                    MIN_GRID_COLUMNS
                )
            }
        }
    }

    companion object {
        private const val MIN_GRID_COLUMNS = 7
    }
}
