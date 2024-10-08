package com.study.auth.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.study.auth.api.Authentificator
import com.study.auth.api.UserNotAuthorizedException
import dagger.Reusable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Reusable
internal class DefaultAuthentificator @Inject constructor(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher
) : Authentificator {
    override suspend fun getEmail(): String {
        return getLocallySaved(EMAIL)
            ?: throw UserNotAuthorizedException("Can't get the email")
    }

    override suspend fun getApiKey(): String {
        return getLocallySaved(API_KEY) ?: throw UserNotAuthorizedException("Can't get the api key")
    }

    override suspend fun saveEmail(email: String) {
        saveLocally(EMAIL, email)
    }

    override suspend fun saveUserId(userId: Int) {
        saveLocally(USER_ID, userId)
    }

    override suspend fun saveApiKey(apiKey: String) {
        saveLocally(API_KEY, apiKey)
    }

    override suspend fun getUserId(): Int = withContext(dispatcher) {
        getLocallySaved(USER_ID) ?: throw UserNotAuthorizedException("Can't get the userId")
    }


    override suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }

    override suspend fun isAuthorized(): Boolean {
        return getLocallySaved(EMAIL) != null
                && getLocallySaved(USER_ID) != null
                && getLocallySaved(API_KEY) != null
    }

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
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            DATA_STORE_NAME
        )
        private val USER_ID = intPreferencesKey("userId")
        private val EMAIL = stringPreferencesKey("email")
        private val API_KEY = stringPreferencesKey("apiKey")
    }
}
