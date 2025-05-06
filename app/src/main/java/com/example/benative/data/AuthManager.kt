package com.example.benative.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
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

    suspend fun isTokenValid(context: Context): Boolean {
        val token = getToken(context).firstOrNull() ?: return false
        return try {
            // Сделай простой защищённый запрос на сервер, например, /me или /user
            val response = ApiModule().get("check-token") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            response.status == HttpStatusCode.OK
        } catch (e: Exception) {
            false
        }
    }
}