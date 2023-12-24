package com.study.auth.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.study.auth.api.UserAuthRepository
import com.study.auth.api.UserNotAuthorizedException
import dagger.Reusable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Reusable
internal class DefaultUserAuthRepository @Inject constructor(
    private val userDataSource: RemoteUserDataSource,
    private val context: Context,
    private val dispatcher: CoroutineDispatcher
) : UserAuthRepository {

    override suspend fun getUserId(): Int = withContext(dispatcher) {
        getCurrentValue(USER_ID) { userDataSource.getCurrentUser().userId }
    } ?: throw UserNotAuthorizedException("Can't get userId")

    private suspend fun <T> getCurrentValue(
        key: Preferences.Key<T>,
        itemProducer: suspend () -> T?
    ): T? = getLocallySaved(key) ?: run {
        val item = itemProducer()
        saveLocally(key, item)
        item
    }

    override suspend fun isAdmin(): Boolean = withContext(dispatcher) {
        getCurrentValue(IS_ADMIN) { requireNotNull(userDataSource.getCurrentUser().isAdmin) }
    } ?: throw UserNotAuthorizedException("Can't get isAdmin")

    private suspend fun <T> getLocallySaved(key: Preferences.Key<T>): T? =
        context.dataStore.data.map { preferences -> preferences[key] }.first()

    private suspend fun <T> saveLocally(key: Preferences.Key<T>, newValue: T?) =
        newValue?.let {
            context.dataStore.edit { preferences ->
                preferences[key] = newValue
            }
        }


    companion object {
        private const val DATA_STORE_NAME = "user info"
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATA_STORE_NAME)
        private val USER_ID = intPreferencesKey("userId")
        private val IS_ADMIN = booleanPreferencesKey("isAdmin")
    }
}
