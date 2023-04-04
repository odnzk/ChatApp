package com.study.profile.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.study.components.BaseScreenStateFragment
import com.study.components.extensions.loadFromUrl
import com.study.profile.databinding.FragmentProfileBinding
import com.study.profile.presentation.model.UiUser

internal class ProfileFragment :
    BaseScreenStateFragment<ProfileViewModel, FragmentProfileBinding, UiUser>() {
    override val binding: FragmentProfileBinding get() = _binding!!
    override val viewModel: ProfileViewModel by viewModels()
    override val onTryAgainClick: View.OnClickListener
        get() = View.OnClickListener { viewModel.reload() }
    private var _binding: FragmentProfileBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        screenStateView = binding.fragmentProfileScreenStateView
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        screenStateView = null
        _binding = null
    }

    override fun onSuccess(data: UiUser) {
        with(binding) {
            fragmentProfileIvAvatar.loadFromUrl(data.avatarUrl)
            fragmentProfileTvIsOnline.run {
                text = getString(data.presence.titleResId).lowercase()
                setTextColor(ContextCompat.getColor(context, data.presence.colorResId))
            }
            fragmentProfileTvUsername.text = data.username
        }
    }

    override fun observeState() {
        viewModel.state.collectScreenStateSafely()
    }
}
