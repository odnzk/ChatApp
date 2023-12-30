package com.study.users.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.study.common.search.NothingFoundForThisQueryException
import com.study.components.customview.ScreenStateView.ViewState
import com.study.components.ext.delegatesToList
import com.study.components.ext.toBaseErrorMessage
import com.study.components.recycler.delegates.GeneralAdapterDelegate
import com.study.users.databinding.FragmentUsersBinding
import com.study.users.di.UsersComponentViewModel
import com.study.users.presentation.elm.UsersEffect
import com.study.users.presentation.elm.UsersEvent
import com.study.users.presentation.elm.UsersState
import com.study.users.presentation.model.UserShimmer
import com.study.users.presentation.util.delegate.UserDelegate
import com.study.users.presentation.util.navigation.navigateToProfileFragment
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.StoreHolder
import javax.inject.Inject

internal class UsersFragment : ElmFragment<UsersEvent, UsersEffect, UsersState>() {
    private val binding: FragmentUsersBinding get() = _binding!!
    private var _binding: FragmentUsersBinding? = null
    private var adapter: GeneralAdapterDelegate? = null
    override val initEvent: UsersEvent = UsersEvent.Ui.Init
    override val storeHolder: StoreHolder<UsersEvent, UsersEffect, UsersState> get() = userStore

    @Inject
    lateinit var userStore: StoreHolder<UsersEvent, UsersEffect, UsersState>


    override fun onAttach(context: Context) {
        ViewModelProvider(this).get<UsersComponentViewModel>().usersComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        setupListeners()
        observeSearchQuery()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        _binding = null
    }

    override fun render(state: UsersState) {
        when {
            state.error != null -> {
                adapter?.submitList(emptyList())
                binding.fragmentIsersScreenStateView.setState(
                    ViewState.Error(
                        state.error.toBaseErrorMessage(),
                        state.error !is NothingFoundForThisQueryException
                    )
                )
            }

            state.isLoading && state.users.isEmpty() -> {
                binding.fragmentIsersScreenStateView.setState(ViewState.Success)
                adapter?.submitList(List(UserShimmer.DEFAULT_SHIMMER_COUNT) { UserShimmer })
            }

            state.isLoading -> binding.fragmentIsersScreenStateView.setState(ViewState.Loading)
            state.users.isNotEmpty() -> {
                binding.fragmentIsersScreenStateView.setState(ViewState.Success)
                adapter?.submitList(state.users)
            }
        }
    }

    private fun setupListeners() {
        binding.fragmentIsersScreenStateView.onTryAgainClickListener = View.OnClickListener {
            store.accept(UsersEvent.Ui.Reload)
        }
    }

    private fun observeSearchQuery() {
        binding.fragmentUsersSearchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                store.accept(UsersEvent.Ui.Search(query.orEmpty()))
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                store.accept(UsersEvent.Ui.Search(newText.orEmpty()))
                return false
            }

        })
    }


    private fun initUI() {
        val userDelegate = UserDelegate { userId -> navigateToProfileFragment(userId) }
        adapter = GeneralAdapterDelegate(delegatesToList(userDelegate))
        with(binding.fragmentUsersRv) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@UsersFragment.adapter
        }
    }
}
