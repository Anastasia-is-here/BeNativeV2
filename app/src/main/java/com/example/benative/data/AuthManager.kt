package com.example.benative.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

@Singleton
class AuthManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val KEY_TOKEN = stringPreferencesKey("token")

    // Сохранение токена
    suspend fun saveToken(token: String) {

        context.authDataStore.edit { preferences ->
            preferences[KEY_TOKEN] = token
        }
    }

    // Получение токена как Flow
    fun getToken(): Flow<String?> {
        return context.authDataStore.data.map { preferences ->
            preferences[KEY_TOKEN]
        }
    }

    // Очистка токена
    suspend fun clearToken() {
        context.authDataStore.edit { preferences ->
            preferences.remove(KEY_TOKEN)
        }
    }

}