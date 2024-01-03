package com.study.profile.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.study.common.ext.fastLazy
import com.study.components.customview.ScreenStateView.ViewState
import com.study.components.ext.delegatesToList
import com.study.components.recycler.SpaceHorizontalDividerItemDecorator
import com.study.components.recycler.delegates.GeneralAdapterDelegate
import com.study.profile.databinding.FragmentProfileBinding
import com.study.profile.di.ProfileComponentViewModel
import com.study.profile.presentation.elm.ProfileActor
import com.study.profile.presentation.elm.ProfileEffect
import com.study.profile.presentation.elm.ProfileEvent
import com.study.profile.presentation.elm.ProfileReducer
import com.study.profile.presentation.elm.ProfileState
import com.study.profile.presentation.model.UiUserDetailed
import com.study.profile.presentation.util.delegate.RoleDelegate
import com.study.profile.presentation.util.toErrorMessage
import com.study.ui.NavConstants
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.coroutines.ElmStoreCompat
import javax.inject.Inject
import com.study.ui.R as CoreR


internal class ProfileFragment : ElmFragment<ProfileEvent, ProfileEffect, ProfileState>() {
    @Inject
    lateinit var actor: ProfileActor

    @Inject
    lateinit var reducerFactory: ProfileReducer.Factory

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding get() = _binding!!
    private val userId: Int? by fastLazy {
        arguments
            ?.getInt(NavConstants.USER_ID_KEY)
            ?.takeIf { userId -> userId != NavConstants.CURRENT_USER_ID_KEY }
    }
    private var adapter: GeneralAdapterDelegate? = null
    override val initEvent: ProfileEvent get() = ProfileEvent.Ui.Init

    override val storeHolder: StoreHolder<ProfileEvent, ProfileEffect, ProfileState> by fastLazy {
        LifecycleAwareStoreHolder(lifecycle) {
            ElmStoreCompat(
                ProfileState(),
                reducerFactory.create(userId),
                actor
            )
        }
    }

    override fun onAttach(context: Context) {
        ViewModelProvider(this).get<ProfileComponentViewModel>().profileComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        _binding = null
    }

    override fun render(state: ProfileState) {
        when {
            state.error != null -> with(binding) {
                fragmentProfileGroupUserInfo.isVisible = false
                screenStateView.setState(ViewState.Error(state.error.toErrorMessage()))
            }

            state.isLoading -> {
                with(binding) {
                    screenStateView.setState(ViewState.Loading)
                }
            }

            state.user != null -> with(binding) {
                fragmentProfileGroupUserInfo.isVisible = true
                screenStateView.setState(ViewState.Success)
                displayUser(state.user)
            }
        }
    }

    private fun displayUser(user: UiUserDetailed) = with(binding) {
        fragmentProfileTvIsOnline.text = getString(user.presence.titleResId).lowercase()
        fragmentProfileTvIsOnline.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                user.presence.colorResId
            )
        )
        if (user.avatarUrl != null) {
            fragmentProfileIvAvatar.load(user.avatarUrl)
        } else if (user.isBot) {
            fragmentProfileIvAvatar.setImageResource(CoreR.drawable.ic_baseline_bot_24)
        }
        fragmentProfileTvUsername.text = user.username
        fragmentProfileTvEmail.text = user.email
        adapter?.submitList(user.roles)
    }

    private fun initUI() = with(binding) {
        fragmentProfileGroupUserInfo.isVisible = false
        screenStateView.onTryAgainClickListener =
            View.OnClickListener { store.accept(ProfileEvent.Ui.Reload) }
        fragmentProfileBtnLogout.setOnClickListener {
            // TODO() implement logout
            //  TODO hide logout if it is not current user
        }
        adapter = GeneralAdapterDelegate(delegatesToList(RoleDelegate()))
        fragmentProfileRvRoles.adapter = adapter
        fragmentProfileRvRoles.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        fragmentProfileRvRoles.setHasFixedSize(true)
        fragmentProfileRvRoles.addItemDecoration(SpaceHorizontalDividerItemDecorator(ITEM_ROLE_SPACE))
    }

    companion object {
        private const val ITEM_ROLE_SPACE = 10
    }
}
