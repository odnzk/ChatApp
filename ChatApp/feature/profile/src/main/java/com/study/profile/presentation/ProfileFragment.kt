package com.study.profile.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.study.common.extensions.fastLazy
import com.study.components.extensions.createStoreHolder
import com.study.components.extensions.loadFromUrl
import com.study.components.view.ScreenStateView.ViewState
import com.study.profile.databinding.FragmentProfileBinding
import com.study.profile.di.ProfileComponentViewModel
import com.study.profile.presentation.elm.ProfileEffect
import com.study.profile.presentation.elm.ProfileEvent
import com.study.profile.presentation.elm.ProfileState
import com.study.profile.presentation.util.toErrorMessage
import com.study.ui.NavConstants
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject


internal class ProfileFragment : ElmFragment<ProfileEvent, ProfileEffect, ProfileState>() {
    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding get() = _binding!!
    private val userId by fastLazy {
        arguments?.getInt(NavConstants.USER_ID_KEY) ?: NavConstants.CURRENT_USER_ID_KEY
    }

    @Inject
    lateinit var profileStore: Store<ProfileEvent, ProfileEffect, ProfileState>


    override val initEvent: ProfileEvent get() = ProfileEvent.Ui.Init(userId)
    override val storeHolder: StoreHolder<ProfileEvent, ProfileEffect, ProfileState> by fastLazy {
        createStoreHolder(profileStore)
    }

    override fun onAttach(context: Context) {
        ViewModelProvider(this).get<ProfileComponentViewModel>()
            .profileComponent.inject(this)
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
            state.error != null -> binding.fragmentProfileScreenStateView.setState(
                ViewState.Error(state.error.toErrorMessage())
            )
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
}
