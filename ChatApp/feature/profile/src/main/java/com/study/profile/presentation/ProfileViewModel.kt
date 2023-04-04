package com.study.profile.presentation

import androidx.lifecycle.SavedStateHandle
import com.study.common.ScreenState
import com.study.components.BaseViewModel
import com.study.components.model.UserPresenceStatus
import com.study.profile.data.RemoteUserRepository
import com.study.profile.domain.model.UserDetailed
import com.study.profile.domain.repository.UserRepository
import com.study.profile.domain.util.UserNotFoundException
import com.study.profile.presentation.mapper.toUiUser
import com.study.profile.presentation.model.UiUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class ProfileViewModel(
    saveStateHandler: SavedStateHandle
) : BaseViewModel<UiUser>() {
    private val repository: UserRepository = RemoteUserRepository()
    private var userId = saveStateHandler.get<Int>("userId") ?: DEFAULT_USER_ID

    override val _state: MutableStateFlow<ScreenState<UiUser>> =
        MutableStateFlow(ScreenState.Loading)
    val state = _state.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() = safeLaunch {
        val user: UserDetailed = if (userId == DEFAULT_USER_ID) {
            repository.getCurrentUser()
        } else {
            repository.getUserById(userId) ?: throw UserNotFoundException()
        }
        userId = user.id
        _state.value = ScreenState.Success(user.toUiUser(getUserPresence(user.isActive)))
    }

    private suspend fun getUserPresence(isActive: Boolean): UserPresenceStatus {
        return if (isActive) {
            repository.getUserPresence(userId)
        } else UserPresenceStatus.OFFLINE
    }

    fun reload() = loadData()

    companion object {
        private const val DEFAULT_USER_ID = -1
    }
}
