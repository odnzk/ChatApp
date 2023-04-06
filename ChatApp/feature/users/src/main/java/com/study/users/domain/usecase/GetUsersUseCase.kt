package com.study.users.domain.usecase

import com.study.components.model.UserPresenceStatus
import com.study.users.domain.repository.UsersRepository
import com.study.users.presentation.model.UiUser
import com.study.users.presentation.util.mapper.toOfflineUsers
import com.study.users.presentation.util.mapper.toUiUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class GetUsersUseCase(
    private val repository: UsersRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): List<UiUser> = withContext(dispatcher) {
        val resList: MutableList<UiUser> = mutableListOf()
        repository.getUsers().groupBy { it.isActive }.forEach { (isActive, users) ->
            if (isActive) {
                val usersPresence = repository.getUsersPresence().sortedBy { it.userEmail }
                users.forEach { activeUser ->
                    val status = usersPresence.find { it.userEmail == activeUser.email }?.let {
                        if (it.isActive) UserPresenceStatus.ACTIVE else UserPresenceStatus.IDLE
                    } ?: UserPresenceStatus.OFFLINE
                    resList.add(activeUser.toUiUser(status))
                }
            } else resList.addAll(users.toOfflineUsers())
        }
        resList.sortedBy { it.presence }
    }
}

