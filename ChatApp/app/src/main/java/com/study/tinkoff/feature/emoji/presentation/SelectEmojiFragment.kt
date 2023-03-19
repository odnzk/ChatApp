package com.study.tinkoff.feature.emoji.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.study.common.rv.delegates.Delegate
import com.study.common.rv.delegates.GeneralAdapterDelegate
import com.study.common.rv.manager.VarSpanGridLayoutManager
import com.study.components.dp
import com.study.components.extensions.collectFlowSafely
import com.study.tinkoff.databinding.FragmentRecyclerViewBinding
import com.study.tinkoff.feature.emoji.presentation.delegates.emoji.EmojiDelegate

class SelectEmojiFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentRecyclerViewBinding? = null
    private val binding get() = _binding!!
    private var mainEmojiAdapter: GeneralAdapterDelegate? = null
    private val viewModel: SelectEmojiViewModel by viewModels()
    private val args by navArgs<SelectEmojiFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecyclerViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainEmojiAdapter = null
        _binding = null
    }

    private fun observeState() {
        collectFlowSafely(viewModel.state) { mainEmojiAdapter?.submitList(it) }
    }

    private fun initUI() {
        mainEmojiAdapter = GeneralAdapterDelegate(listOf(EmojiDelegate(onEmojiClick = { emojiName ->
            setFragmentResult(
                args.resultKey,
                bundleOf(args.resultKey to emojiName)
            )
            dismiss()
        }
        )) as List<Delegate<RecyclerView.ViewHolder, Any>>)
        with(binding.fragmentRvDataList) {
            adapter = mainEmojiAdapter
            layoutManager = VarSpanGridLayoutManager(
                context,
                80f.dp(context),
                MIN_GRID_COLUMNS
            )
        }
    }

    companion object {
        private const val MIN_GRID_COLUMNS = 8
    }
}
