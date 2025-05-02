package com.example.benative.server

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:8080/" // Для эмулятора

    val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = false
                encodeDefaults = true
                explicitNulls = true
            })
        }
    }

    const val loginUrl = "$BASE_URL/login"
    const val profileUrl = "$BASE_URL/me"
    const val lessonsUrl = "$BASE_URL/lessons"
    fun tasksUrl(lessonId: Int) = "$BASE_URL/lessons/$lessonId/tasks"
}