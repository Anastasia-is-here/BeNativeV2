package com.example.benative.server

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Создаём DataStore с именем "auth_prefs"
val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

object AuthManager {
    private val KEY_TOKEN = stringPreferencesKey("token")

    // Сохранение токена
    suspend fun saveToken(context: Context, token: String) {
        context.authDataStore.edit { preferences ->
            preferences[KEY_TOKEN] = token
        }
    }

    // Получение токена как Flow
    fun getToken(context: Context): Flow<String?> {
        return context.authDataStore.data.map { preferences ->
            preferences[KEY_TOKEN]
        }
    }

    // Очистка токена
    suspend fun clearToken(context: Context) {
        context.authDataStore.edit { preferences ->
            preferences.remove(KEY_TOKEN)
        }
    }
}