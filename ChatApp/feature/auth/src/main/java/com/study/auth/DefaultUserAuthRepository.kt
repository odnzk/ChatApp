package com.study.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.study.network.impl.ZulipApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class DefaultUserAuthRepository(private val api: ZulipApi, private val context: Context) :
    UserAuthRepository {

    private suspend fun getLocallySaved(): Int? =
        context.dataStore.data.map { preferences ->
            preferences[USER_ID]
        }.first()


    private suspend fun saveLocally(userId: Int) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }

    override suspend fun getCurrentUserId(): Int {
        return getLocallySaved() ?: run {
            val userId = requireNotNull(api.getCurrentUser().userId)
            saveLocally(userId)
            userId
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
