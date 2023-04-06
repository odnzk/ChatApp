package com.study.users.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.study.components.BaseScreenStateFragment
import com.study.components.recycler.delegates.Delegate
import com.study.components.recycler.delegates.GeneralAdapterDelegate
import com.study.search.databinding.FragmentSearchBinding
import com.study.ui.R
import com.study.users.presentation.model.UiUser
import com.study.users.presentation.model.UserShimmer
import com.study.users.presentation.util.delegates.UserDelegate
import com.study.users.presentation.util.navigation.navigateToProfileFragment

internal class UsersFragment :
    BaseScreenStateFragment<UsersViewModel, FragmentSearchBinding, List<UiUser>>() {
    override val viewModel: UsersViewModel by viewModels()
    override val binding: FragmentSearchBinding get() = _binding!!
    override val onTryAgainClick: View.OnClickListener
        get() = View.OnClickListener {
            viewModel.onEvent(UsersFragmentEvent.Reload)
        }

    private var _binding: FragmentSearchBinding? = null
    private var adapter: GeneralAdapterDelegate? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        screenStateView = binding.fragmentSearchScreenStateView
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        screenStateView = null
        adapter = null
        _binding = null
    }

    override fun observeState() {
        viewModel.state.collectScreenStateSafely()
    }

    override fun onLoading() {
        adapter?.submitList(List(UserShimmer.DEFAULT_SHIMMER_COUNT) { UserShimmer })
    }

    override fun setupListeners() {
        with(binding) {
            fragmentSearchSearchView.searchHint = getString(R.string.fragment_users_search_query)
            fragmentSearchSearchView.afterChangeTextListener = { query ->
                query?.let { viewModel.onEvent(UsersFragmentEvent.Search(it.toString())) }
            }
        }
        val userDelegate = UserDelegate { userId ->
            navigateToProfileFragment(userId)
        }
        adapter =
            GeneralAdapterDelegate(listOf(userDelegate) as List<Delegate<RecyclerView.ViewHolder, Any>>)
        binding.fragmentSearchRvData.adapter = adapter
    }

    override fun initUI() {
        with(binding.fragmentSearchRvData) {
            layoutManager = LinearLayoutManager(context)
            adapter = adapter
        }
    }

    override fun onSuccess(data: List<UiUser>) {
        adapter?.submitList(data)
    }
}
