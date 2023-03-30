package com.study.profile.presentation

import androidx.lifecycle.SavedStateHandle
import com.study.components.BaseViewModel
import com.study.components.ScreenState
import com.study.profile.data.StubUserAuthRepository
import com.study.profile.data.StubUserRepository
import com.study.profile.domain.repository.UserAuthRepository
import com.study.profile.domain.repository.UserRepository
import com.study.profile.domain.util.UserNotAuthorizedException
import com.study.profile.domain.util.UserNotFoundException
import com.study.profile.presentation.mapper.toUiUser
import com.study.profile.presentation.model.UiUser
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest

internal class ProfileViewModel(
    private val saveStateHandler: SavedStateHandle
) : BaseViewModel<UiUser>() {
    private val authRepository: UserAuthRepository = StubUserAuthRepository()
    private val usersRepository: UserRepository = StubUserRepository()
    private val userId get() = saveStateHandler.get<Int>("userId") ?: DEFAULT_USER_ID
    private var jobObservingUser: Job? = null
    override val _state: MutableStateFlow<ScreenState<UiUser>> =
        MutableStateFlow(ScreenState.Loading)
    val state = _state.asStateFlow()

    init {
        loadData(userId)
    }

    private fun loadData(userId: Int) {
        jobObservingUser?.cancel()
        jobObservingUser = safeLaunch {
            if (userId == DEFAULT_USER_ID) {
                authRepository.getCurrentUser().collectLatest { user ->
                    _state.value = user?.let { ScreenState.Success(it.toUiUser()) }
                        ?: ScreenState.Error(
                            UserNotAuthorizedException()
                        )
                }
            } else {
                usersRepository.getUserById(userId).collectLatest { user ->
                    _state.value = user?.let { ScreenState.Success(user.toUiUser()) }
                        ?: ScreenState.Error(UserNotFoundException())
                }
            }
        }
    }

    fun onEvent(event: ProfileFragmentEvent) {
        safeLaunch {
            when (event) {
                ProfileFragmentEvent.Logout -> authRepository.logout()
                ProfileFragmentEvent.Reload -> loadData(userId)
            }
        }
    }

    companion object {
        private const val DEFAULT_USER_ID = -1
    }
}
