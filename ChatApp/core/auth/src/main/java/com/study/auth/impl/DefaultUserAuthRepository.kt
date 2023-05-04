package com.study.auth.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.study.auth.api.UserAuthRepository
import com.study.network.dataSource.UserDataSource
import dagger.Reusable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Reusable
internal class DefaultUserAuthRepository @Inject constructor(
    private val userDataSource: UserDataSource,
    private val context: Context,
    private val dispatcher: CoroutineDispatcher
) :
    UserAuthRepository {

    override suspend fun getCurrentUserId(): Int = withContext(dispatcher) {
        getLocallySaved() ?: run {
            val userId = requireNotNull(userDataSource.getCurrentUser().userId)
            saveLocally(userId)
            userId
        }
    }

    private suspend fun getLocallySaved(): Int? =
        context.dataStore.data.map { preferences -> preferences[USER_ID] }.first()


    private suspend fun saveLocally(userId: Int) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }

    companion object {
        private const val DATA_STORE_NAME = "user info"
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            DATA_STORE_NAME
        )
        private val USER_ID = intPreferencesKey("userId")
    }
}
