package com.study.tinkoff.feature.select_emoji.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.study.tinkoff.core.ui.extensions.dp
import com.study.tinkoff.core.ui.rv.delegates.DelegateItem
import com.study.tinkoff.core.ui.rv.delegates.MainAdapter
import com.study.tinkoff.core.ui.rv.manager.VarSpanGridLayoutManager
import com.study.tinkoff.databinding.FragmentSelectEmojiBinding
import com.study.tinkoff.feature.chat.presentation.ChatFragment
import com.study.tinkoff.feature.select_emoji.data.StubEmojiRepository
import com.study.tinkoff.feature.select_emoji.domain.EmojiRepository
import com.study.tinkoff.feature.select_emoji.presentation.delegates.EmojiDelegateAdapter
import com.study.tinkoff.feature.select_emoji.presentation.delegates.toEmojiDelegateItems

class SelectEmojiFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentSelectEmojiBinding? = null
    private val binding get() = _binding!!

    private val emojiRepository: EmojiRepository = StubEmojiRepository
    private var mainEmojiAdapter: MainAdapter? = MainAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectEmojiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainEmojiAdapter?.apply {
            val emojiAdapter = EmojiDelegateAdapter().apply {
                onEmojiClickListener = { emojiName ->
                    setFragmentResult(
                        ChatFragment.SELECTED_EMOJI_RESULT,
                        bundleOf(ChatFragment.SELECTED_EMOJI_NAME_KEY to emojiName)
                    )
                    dismiss()
                }
                (emojiRepository.getEmoji()
                    .toEmojiDelegateItems() as? List<DelegateItem<Any>>)?.let {
                    submitList(it)
                }
            }
            addDelegate(emojiAdapter)
        }
        with(binding.rvEmojiList) {
            adapter = mainEmojiAdapter
            layoutManager =
                VarSpanGridLayoutManager(context, 80f.dp(context), MIN_GRID_COLUMNS)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainEmojiAdapter = null
        _binding = null
    }

    companion object {
        private const val MIN_GRID_COLUMNS = 8
    }
}
