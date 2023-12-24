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
import androidx.navigation.fragment.findNavController
import coil.load
import com.study.common.ext.fastLazy
import com.study.components.customview.ScreenStateView.ViewState
import com.study.profile.databinding.FragmentProfileBinding
import com.study.profile.di.ProfileComponentViewModel
import com.study.profile.presentation.elm.ProfileEffect
import com.study.profile.presentation.elm.ProfileEvent
import com.study.profile.presentation.elm.ProfileState
import com.study.profile.presentation.model.UiUserDetailed
import com.study.profile.presentation.util.toErrorMessage
import com.study.ui.NavConstants
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.StoreHolder
import javax.inject.Inject
import com.study.ui.R as CoreR


internal class ProfileFragment : ElmFragment<ProfileEvent, ProfileEffect, ProfileState>() {
    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding get() = _binding!!
    private val userId by fastLazy {
        arguments?.getInt(NavConstants.USER_ID_KEY) ?: NavConstants.CURRENT_USER_ID_KEY
    }
    override val initEvent: ProfileEvent get() = ProfileEvent.Ui.Init(userId)
    override val storeHolder: StoreHolder<ProfileEvent, ProfileEffect, ProfileState> get() = profileStore

    @Inject
    lateinit var profileStore: StoreHolder<ProfileEvent, ProfileEffect, ProfileState>

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
        _binding = null
    }

    override fun render(state: ProfileState) {
        when {
            state.error != null -> with(binding) {
                fragmentProfileGroupUserInfo.isVisible = false
                screenStateView.setState(ViewState.Error(state.error.toErrorMessage()))
            }
            state.isLoading -> binding.screenStateView.setState(ViewState.Loading)
            state.user != null -> with(binding) {
                fragmentProfileGroupUserInfo.isVisible = true
                screenStateView.setState(ViewState.Success)
                displayUser(state.user)
            }
        }
    }

    private fun displayUser(user: UiUserDetailed) = with(binding) {
        fragmentProfileTvIsOnline.run {
            text = getString(user.presence.titleResId).lowercase()
            setTextColor(ContextCompat.getColor(context, user.presence.colorResId))
        }
        if (user.avatarUrl != null) {
            fragmentProfileIvAvatar.load(user.avatarUrl)
        } else if (user.isBot) {
            fragmentProfileIvAvatar.setImageResource(CoreR.drawable.ic_baseline_bot_24)
        }
        fragmentProfileTvUsername.text = user.username
    }

    private fun initUI() = with(binding) {
        fragmentProfileGroupUserInfo.isVisible = false
        screenStateView.onTryAgainClickListener =
            View.OnClickListener { store.accept(ProfileEvent.Ui.Reload(userId)) }
        fragmentProfileGroupToolbar.isVisible = if (userId != NavConstants.CURRENT_USER_ID_KEY) {
            fragmentProfileBtnBack.setOnClickListener { findNavController().popBackStack() }
            true
        } else false
    }
}
