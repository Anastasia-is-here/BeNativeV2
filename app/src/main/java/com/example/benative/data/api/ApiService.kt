package com.example.benative.data.api

import com.example.benative.data.stringOrException
import com.example.benative.domain.Lesson
import com.example.benative.domain.LessonCompletionRequest
import com.example.benative.domain.LoginRequest
import com.example.benative.domain.LoginResponse
import com.example.benative.domain.RegisterRequest
import com.example.benative.domain.Task
import com.example.benative.domain.User
import com.example.benative.domain.UserStatsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiService @Inject constructor(
    private val client: HttpClient
) {

    suspend fun getLesson(token: String, lessonId: Int): Lesson = client.get("lessons/$lessonId"){
        header(HttpHeaders.Authorization, "Bearer $token")
    }.body()

    suspend fun getTasks(token: String, lessonId: Int): List<Task> = client.get("lessons/$lessonId/tasks"){
        header(HttpHeaders.Authorization, "Bearer $token")
    }.body()

    suspend fun logIn(body: LoginRequest): LoginResponse {
        val rawJson = client.post("login"){
            contentType(ContentType.Application.Json)
            setBody(body)
        }.stringOrException()

        return Json.decodeFromString(rawJson)
    }

    suspend fun register(body: RegisterRequest): LoginResponse {
        val rawJson = client.post("register") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.stringOrException()

        return Json.decodeFromString(rawJson)
    }

    suspend fun getUser(token: String): User = client.get("me"){
        header(HttpHeaders.Authorization, "Bearer $token")
        contentType(ContentType.Application.Json)
    }.body()

    suspend fun getLessons(token: String): List<Lesson> = client.get("lessons"){
        header(HttpHeaders.Authorization, "Bearer $token")
    }.body()

    suspend fun completeLesson(token: String, body: LessonCompletionRequest) {
        client.post("lessons/${body.lessonId}/complete") {
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(body)
        }
    }

    suspend fun getUserStats(token: String): UserStatsResponse = client.get("user/stats") {
        header(HttpHeaders.Authorization, "Bearer $token")
        contentType(ContentType.Application.Json)
    }.body()

    suspend fun uploadAvatar(token: String, file: File) {
        client.post("user/avatar") {
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(MultiPartFormDataContent(
                formData {
                    append("avatar", file.readBytes(), Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg") // Указываем тип файла
                        append(HttpHeaders.ContentDisposition, "filename=\"${file.name}\"")
                    })
                }
            ))
        }
    }

    suspend fun deleteAvatar(token: String){
        client.delete("user/avatar"){
            header(HttpHeaders.Authorization, "Bearer $token")
        }
    }

    suspend fun updateUserName(token: String, newName: String) {
        client.put("user/name") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(mapOf("name" to newName))
        }
    }
}
