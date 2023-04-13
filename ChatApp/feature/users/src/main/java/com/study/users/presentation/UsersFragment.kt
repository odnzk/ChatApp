package com.study.users.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.study.common.extensions.fastLazy
import com.study.components.databinding.FragmentRecyclerViewBinding
import com.study.components.extensions.createStoreHolder
import com.study.components.extensions.delegatesToList
import com.study.components.extensions.toBaseErrorMessage
import com.study.components.recycler.delegates.GeneralAdapterDelegate
import com.study.components.view.ScreenStateView.ViewState
import com.study.network.NetworkModule
import com.study.users.domain.repository.UsersRepository
import com.study.users.domain.usecase.GetUsersPresenceCase
import com.study.users.domain.usecase.GetUsersUseCase
import com.study.users.domain.usecase.SearchUsersUseCase
import com.study.users.presentation.elm.UsersEffect
import com.study.users.presentation.elm.UsersEvent
import com.study.users.presentation.elm.UsersState
import com.study.users.presentation.elm.UsersStoreFactory
import com.study.users.presentation.util.delegates.UserDelegate
import com.study.users.presentation.util.model.UserShimmer
import com.study.users.presentation.util.navigation.navigateToProfileFragment
import kotlinx.coroutines.Dispatchers
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.StoreHolder

internal class UsersFragment : ElmFragment<UsersEvent, UsersEffect, UsersState>() {
    private val binding: FragmentRecyclerViewBinding get() = _binding!!
    private var _binding: FragmentRecyclerViewBinding? = null
    private var adapter: GeneralAdapterDelegate? = null

    override val initEvent: UsersEvent = UsersEvent.Ui.Init
    override val storeHolder: StoreHolder<UsersEvent, UsersEffect, UsersState> by fastLazy {
        val repository = UsersRepository(NetworkModule.providesApi())
        val dispatcher = Dispatchers.Default
        createStoreHolder(
            UsersStoreFactory.create(
                GetUsersUseCase(repository, dispatcher),
                GetUsersPresenceCase(repository, dispatcher),
                SearchUsersUseCase(repository, dispatcher)
            )
        )
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        _binding = null
    }

    override fun render(state: UsersState) {
        when {
            state.isLoading && state.users.isEmpty() -> {
                with(binding) {
                    screenStateView.setState(ViewState.Success)
                    fragmentRvDataList.isVisible = true
                }
                adapter?.submitList(List(UserShimmer.DEFAULT_SHIMMER_COUNT) { UserShimmer })
            }
            state.users.isNotEmpty() -> {
                with(binding) {
                    screenStateView.setState(ViewState.Success)
                    fragmentRvDataList.isVisible = true
                }
                adapter?.submitList(state.users)
            }
        }
    }

    override fun handleEffect(effect: UsersEffect) {
        when (effect) {
            is UsersEffect.ShowError -> with(binding) {
                screenStateView.setState(ViewState.Error(effect.error.toBaseErrorMessage()))
                fragmentRvDataList.isVisible = false
            }
        }
    }

    private fun setupListeners() {
        binding.screenStateView.onTryAgainClickListener = View.OnClickListener {
            store.accept(UsersEvent.Ui.Reload)
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
