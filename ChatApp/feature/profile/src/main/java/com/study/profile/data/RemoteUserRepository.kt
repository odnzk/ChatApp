package com.study.profile.data

import com.study.components.model.UserPresenceStatus
import com.study.network.repository.UserDataSource
import com.study.profile.data.mapper.toDetailedUser
import com.study.profile.data.mapper.toUserPresenceStatus
import com.study.profile.domain.model.UserDetailed
import com.study.profile.domain.repository.UserRepository
import dagger.Reusable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Reusable
internal class RemoteUserRepository @Inject constructor(
    private val dataSource: UserDataSource,
    private val dispatcher: CoroutineDispatcher
) :
    UserRepository {
    override suspend fun getUserById(id: Int): UserDetailed? = withContext(dispatcher) {
        dataSource.getUserById(id).user?.toDetailedUser()
    }

    override suspend fun getCurrentUser(): UserDetailed = withContext(dispatcher) {
        dataSource.getCurrentUser().toDetailedUser()
    }

    override suspend fun getUserPresence(userId: Int): UserPresenceStatus =
        withContext(dispatcher) { dataSource.getUserPresence(userId).toUserPresenceStatus() }

}
