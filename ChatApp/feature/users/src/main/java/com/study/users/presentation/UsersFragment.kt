package com.study.users.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.study.components.BaseScreenStateFragment
import com.study.components.view.ScreenStateView
import com.study.search.databinding.FragmentSearchBinding
import com.study.ui.R
import com.study.users.domain.model.User
import com.study.users.presentation.mapper.toUserState
import com.study.users.presentation.rv.UserState
import com.study.users.presentation.rv.UsersAdapter

internal class UsersFragment :
    BaseScreenStateFragment<UsersViewModel, FragmentSearchBinding, List<User>>() {
    override val viewModel: UsersViewModel by viewModels()
    override val binding: FragmentSearchBinding get() = _binding!!
    override val onTryAgainClick: View.OnClickListener
        get() = View.OnClickListener { viewModel.onEvent(UsersFragmentEvent.Reload) }
    override val screenStateView: ScreenStateView get() = binding.fragmentSearchScreenStateView
    private var _binding: FragmentSearchBinding? = null
    private var usersAdapter: UsersAdapter? = UsersAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        usersAdapter = null
        _binding = null
    }

    override fun observeState() {
        viewModel.state.collectScreenStateSafely()
    }

    override fun onLoading() {
        val shimmerList = buildList<UserState> {
            repeat(DEFAULT_SHIMMER_COUNT) {
                add(UserState.Loading)
            }
        }
        usersAdapter?.submitList(shimmerList)
    }

    override fun setupListeners() {
        with(binding) {
            fragmentSearchSearchView.searchHint = getString(R.string.fragment_users_search_query)
            fragmentSearchSearchView.afterChangeTextListener = { query ->
                query?.let { viewModel.onEvent(UsersFragmentEvent.Search(it.toString())) }
            }
        }
        usersAdapter?.onUserClick = { userId -> }
    }

    override fun initUI() {
        with(binding.fragmentSearchRvData) {
            layoutManager = LinearLayoutManager(context)
            adapter = usersAdapter
        }
    }

    override fun onSuccess(data: List<User>) {
        usersAdapter?.submitList(data.toUserState())
    }


    companion object {
        private const val DEFAULT_SHIMMER_COUNT = 8
    }
}
