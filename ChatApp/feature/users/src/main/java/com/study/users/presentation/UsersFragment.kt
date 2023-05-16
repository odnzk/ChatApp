package com.study.users.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.study.common.search.NothingFoundForThisQueryException
import com.study.components.databinding.FragmentRecyclerViewBinding
import com.study.components.extension.collectFlowSafely
import com.study.components.extension.delegatesToList
import com.study.components.extension.toBaseErrorMessage
import com.study.components.recycler.delegates.GeneralAdapterDelegate
import com.study.components.view.ScreenStateView.ViewState
import com.study.users.di.UsersComponentViewModel
import com.study.users.presentation.elm.UsersEffect
import com.study.users.presentation.elm.UsersEvent
import com.study.users.presentation.elm.UsersState
import com.study.users.presentation.model.UserShimmer
import com.study.users.presentation.util.delegate.UserDelegate
import com.study.users.presentation.util.navigation.navigateToProfileFragment
import kotlinx.coroutines.flow.Flow
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.StoreHolder
import javax.inject.Inject

internal class UsersFragment : ElmFragment<UsersEvent, UsersEffect, UsersState>() {
    private val binding: FragmentRecyclerViewBinding get() = _binding!!
    private var _binding: FragmentRecyclerViewBinding? = null
    private var adapter: GeneralAdapterDelegate? = null
    override val initEvent: UsersEvent = UsersEvent.Ui.Init
    override val storeHolder: StoreHolder<UsersEvent, UsersEffect, UsersState> get() = userStore

    @Inject
    lateinit var userStore: StoreHolder<UsersEvent, UsersEffect, UsersState>

    @Inject
    lateinit var searchFlow: Flow<String>


    override fun onAttach(context: Context) {
        ViewModelProvider(this).get<UsersComponentViewModel>()
            .usersComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecyclerViewBinding.inflate(inflater, container, false)
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
                binding.screenStateView.setState(
                    ViewState.Error(
                        state.error.toBaseErrorMessage(),
                        state.error !is NothingFoundForThisQueryException
                    )
                )
            }
            state.isLoading && state.users.isEmpty() -> {
                binding.screenStateView.setState(ViewState.Success)
                adapter?.submitList(List(UserShimmer.DEFAULT_SHIMMER_COUNT) { UserShimmer })
            }
            state.isLoading -> binding.screenStateView.setState(ViewState.Loading)
            state.users.isNotEmpty() -> {
                binding.screenStateView.setState(ViewState.Success)
                adapter?.submitList(state.users)
            }
        }
    }

    private fun setupListeners() {
        binding.screenStateView.onTryAgainClickListener = View.OnClickListener {
            store.accept(UsersEvent.Ui.Reload)
        }
    }

    private fun observeSearchQuery() {
        collectFlowSafely(searchFlow, Lifecycle.State.RESUMED) {
            store.accept(UsersEvent.Ui.Search(it))
        }
    }


    private fun initUI() {
        val userDelegate = UserDelegate { userId -> navigateToProfileFragment(userId) }
        adapter = GeneralAdapterDelegate(delegatesToList(userDelegate))
        with(binding.fragmentRvDataList) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@UsersFragment.adapter
        }
    }
}
