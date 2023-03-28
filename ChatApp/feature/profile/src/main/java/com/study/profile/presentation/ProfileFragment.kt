package com.study.profile.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.study.common.extensions.loadUrl
import com.study.components.BaseScreenStateFragment
import com.study.components.view.ScreenStateView
import com.study.profile.databinding.FragmentProfileBinding
import com.study.profile.presentation.model.UiUser

internal class ProfileFragment :
    BaseScreenStateFragment<ProfileViewModel, FragmentProfileBinding, UiUser>() {
    override val binding: FragmentProfileBinding get() = _binding!!
    override val viewModel: ProfileViewModel by viewModels()
    override val screenStateView: ScreenStateView get() = binding.fragmentProfileScreenStateView
    override val onTryAgainClick: View.OnClickListener
        get() = View.OnClickListener { viewModel.onEvent(ProfileFragmentEvent.Reload) }
    private var _binding: FragmentProfileBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSuccess(data: UiUser) {
        with(binding) {
            fragmentProfileIvAvatar.loadUrl(data.avatarUrl)
            fragmentProfileTvStatus.text = data.isActiveStatus
            fragmentProfileTvIsOnline.text = data.isOnMeetingStatus
            fragmentProfileTvUsername.text = data.username
        }
    }

    override fun observeState() {
        viewModel.state.collectScreenStateSafely()
    }

    override fun setupListeners() {
        with(binding) {
            fragmentProfileBtnLogout.setOnClickListener {
                viewModel.onEvent(ProfileFragmentEvent.Logout)
            }
        }
    }

    override fun initUI() = Unit
}
