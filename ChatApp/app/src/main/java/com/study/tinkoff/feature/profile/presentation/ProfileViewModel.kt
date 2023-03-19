package com.study.tinkoff.feature.profile.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.study.components.ScreenState
import com.study.domain.model.User
import com.study.tinkoff.common.ResourceProvider
import com.study.tinkoff.di.StubDiContainer
import com.study.tinkoff.feature.profile.domain.UserAuthRepository
import com.study.tinkoff.feature.profile.domain.UserNotAuthorizedException
import com.study.tinkoff.feature.profile.domain.UserNotFoundException
import com.study.tinkoff.feature.profile.presentation.model.UiUser
import com.study.tinkoff.feature.users.domain.UsersRepository
import com.study.ui.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(private val saveStateHandler: SavedStateHandle) : ViewModel() {
    private val authRepository: UserAuthRepository = StubDiContainer.bindsUserAuthRepository()
    private val usersRepository: UsersRepository = StubDiContainer.bindsUsersRepository()
    private val resourceProvider: ResourceProvider = StubDiContainer.bindsResourceProvider()
    private val _state: MutableStateFlow<ScreenState<UiUser>> =
        MutableStateFlow(ScreenState.Loading)
    val state = _state.asStateFlow()
    private val userId get() = saveStateHandler.get<Int>("userId") ?: DEFAULT_USER_ID

    init {
        loadData(userId)
    }

    private fun loadData(userId: Int) = viewModelScope.launch {
        if (userId == DEFAULT_USER_ID) _state.update {
            authRepository.getCurrentUser()
                ?.let { ScreenState.Success(it.toUiUser(resourceProvider)) } ?: ScreenState.Error(
                UserNotAuthorizedException()
            )
        }
        else {
            _state.update {
                usersRepository.getUserById(userId)?.let { user ->
                    ScreenState.Success(user.toUiUser(resourceProvider))
                } ?: ScreenState.Error(UserNotFoundException())
            }
        }
    }

    fun onEvent(event: ProfileFragmentEvent) = viewModelScope.launch {
        when (event) {
            ProfileFragmentEvent.Logout -> authRepository.logout()
            ProfileFragmentEvent.Reload -> loadData(userId)
        }
    }

    private fun User.toUiUser(provider: ResourceProvider): UiUser {
        val activeStatusId =
            if (is_active) R.string.user_is_active_status_online else R.string.user_is_active_status_offline
        val isOnMeetingStatus =
            if (is_active) R.string.user_is_on_meeting_status_on_meeting else R.string.user_is_on_meeting_status_somewhere_else
        return UiUser(
            username = name,
            avatarUrl = avatarUrl,
            isOnMeetingStatus = provider.getString(isOnMeetingStatus),
            isActiveStatus = provider.getString(activeStatusId)
        )
    }


    companion object {
        private const val DEFAULT_USER_ID = -1
    }
}
