package com.study.tinkoff.feature.users.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.study.components.BaseScreenStateFragment
import com.study.components.view.ScreenStateView
import com.study.domain.model.User
import com.study.tinkoff.databinding.FragmentRecyclerViewBinding
import com.study.tinkoff.feature.users.presentation.rv.UsersAdapter

class UsersFragment :
    BaseScreenStateFragment<UsersViewModel, FragmentRecyclerViewBinding, List<User>>() {
    override val viewModel: UsersViewModel by viewModels()
    private var _binding: FragmentRecyclerViewBinding? = null
    override val binding: FragmentRecyclerViewBinding get() = _binding!!
    private var usersAdapter: UsersAdapter? = UsersAdapter()
    override val screenStateView: ScreenStateView get() = binding.fragmentScreenStateView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecyclerViewBinding.inflate(inflater, container, false)
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

    override fun setupListeners() {
        usersAdapter?.onUserClick = { userId ->
            findNavController().navigate(
                UsersFragmentDirections.actionUsersFragmentToProfileFragment(
                    userId
                )
            )
        }
    }

    override fun initUI() {
        with(binding.fragmentRvDataList) {
            layoutManager = LinearLayoutManager(context)
            adapter = usersAdapter
        }
    }

    override fun onError(error: Throwable) = Unit

    override fun onLoading() = Unit

    override fun onSuccess(data: List<User>) {
        usersAdapter?.submitList(data)
    }
}
