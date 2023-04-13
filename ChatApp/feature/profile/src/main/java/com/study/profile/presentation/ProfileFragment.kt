package com.study.profile.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.study.common.extensions.fastLazy
import com.study.components.extensions.createStoreHolder
import com.study.components.extensions.loadFromUrl
import com.study.components.view.ScreenStateView.ViewState
import com.study.network.NetworkModule
import com.study.profile.databinding.FragmentProfileBinding
import com.study.profile.domain.exceptions.toErrorMessage
import com.study.profile.domain.repository.UserRepository
import com.study.profile.domain.usecase.GetUserPresenceUseCase
import com.study.profile.domain.usecase.GetUserUseCase
import com.study.profile.presentation.elm.ProfileEffect
import com.study.profile.presentation.elm.ProfileEvent
import com.study.profile.presentation.elm.ProfileState
import com.study.profile.presentation.elm.ProfileStoreFactory
import com.study.ui.NavConstants
import kotlinx.coroutines.Dispatchers
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.StoreHolder

internal class ProfileFragment : ElmFragment<ProfileEvent, ProfileEffect, ProfileState>() {
    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding get() = _binding!!
    private val userId by fastLazy {
        arguments?.getInt(NavConstants.USER_ID_KEY) ?: NavConstants.CURRENT_USER_ID_KEY
    }

    override val initEvent: ProfileEvent get() = ProfileEvent.Ui.Init(userId)
    override val storeHolder: StoreHolder<ProfileEvent, ProfileEffect, ProfileState> by fastLazy {
        val repository = UserRepository(NetworkModule.providesApi())
        createStoreHolder(
            ProfileStoreFactory.create(
                GetUserUseCase(repository, Dispatchers.Default),
                GetUserPresenceUseCase(repository, Dispatchers.Default)
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentProfileScreenStateView.onTryAgainClickListener = View.OnClickListener {
            store.accept(ProfileEvent.Ui.Reload(userId))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun render(state: ProfileState) {
        when {
            state.isLoading -> binding.fragmentProfileScreenStateView.setState(ViewState.Loading)
            state.user != null -> {
                with(binding) {
                    fragmentProfileScreenStateView.setState(ViewState.Success)
                    val user = state.user
                    fragmentProfileIvAvatar.loadFromUrl(user.avatarUrl)
                    fragmentProfileTvIsOnline.run {
                        text = getString(user.presence.titleResId).lowercase()
                        setTextColor(ContextCompat.getColor(context, user.presence.colorResId))
                    }
                    fragmentProfileTvUsername.text = user.username
                }
            }
        }
    }

    override fun handleEffect(effect: ProfileEffect) {
        when (effect) {
            is ProfileEffect.ShowError -> {
                binding.fragmentProfileScreenStateView.setState(
                    ViewState.Error(effect.error.toErrorMessage())
                )
            }
        }
    }
}
