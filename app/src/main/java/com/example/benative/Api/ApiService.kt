package com.example.benative.Api

import com.example.benative.server.Lesson
import com.example.benative.server.LoginRequest
import com.example.benative.server.TaskResponse
import com.example.benative.server.UserResponse
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.headers

object ApiService {

    suspend fun getLesson(token: String, lessonId: Int): Lesson = ApiModule().get("lessons/$lessonId"){
        header(HttpHeaders.Authorization, "Bearer $token")
    }.body()

    suspend fun getTasks(token: String, lessonId: Int): List<TaskResponse>  = ApiModule().get("lessons/$lessonId/tasks"){
        header(HttpHeaders.Authorization, "Bearer $token")
    }.body()

    suspend fun logIn(body: LoginRequest): HttpResponse = ApiModule().post("login"){
        contentType(ContentType.Application.Json)
        setBody(body)
    }.body()

    suspend fun getUser(token: String): UserResponse = ApiModule().get("me"){
        header(HttpHeaders.Authorization, "Bearer $token")
        contentType(ContentType.Application.Json)
    }.body()
}
