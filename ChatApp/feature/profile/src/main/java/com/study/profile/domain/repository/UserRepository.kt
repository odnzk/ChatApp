package com.study.profile.domain.repository

import com.study.profile.domain.model.UserDetailed
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserById(id: Int): Flow<UserDetailed?>
}
