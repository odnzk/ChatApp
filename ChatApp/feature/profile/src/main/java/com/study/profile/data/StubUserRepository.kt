package com.study.profile.data

import com.study.profile.domain.model.UserDetailed
import com.study.profile.domain.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class StubUserRepository : UserRepository {
    override fun getUserById(id: Int): Flow<UserDetailed> =
        flow {
            if (Random.nextBoolean()) {
                throw RuntimeException()
            }
            delay(2000)
            UserDetailed(0, "Darrell2", "Test@gmail.com", "", true)
        }
}
