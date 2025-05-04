package com.example.benative.Api

import androidx.annotation.OptIn
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.benative.server.Lesson
import com.example.benative.server.LoginRequest
import com.example.benative.server.LoginResponse
import com.example.benative.server.Task
import com.example.benative.server.User
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

object ApiService {

    suspend fun getLesson(token: String, lessonId: Int): Lesson = ApiModule().get("lessons/$lessonId"){
        header(HttpHeaders.Authorization, "Bearer $token")
    }.body()

    suspend fun getTasks(token: String, lessonId: Int): List<Task> = ApiModule().get("lessons/$lessonId/tasks"){
        header(HttpHeaders.Authorization, "Bearer $token")
    }.body()

    suspend fun logIn(body: LoginRequest): LoginResponse = ApiModule().post("login"){
        contentType(ContentType.Application.Json)
        setBody(body)
    }.body()

    @OptIn(UnstableApi::class)
    suspend fun getUser(token: String): User = ApiModule().get("me"){
        Log.d("Auth", "Token: $token")
        header(HttpHeaders.Authorization, "Bearer $token")
        contentType(ContentType.Application.Json)
    }.body()

    suspend fun getLessons(token: String): List<Lesson> = ApiModule().get("lessons"){
        header(HttpHeaders.Authorization, "Bearer $token")
    }.body()
}
