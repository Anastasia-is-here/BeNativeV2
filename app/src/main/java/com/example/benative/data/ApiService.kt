package com.example.benative.data

import com.example.benative.domain.Lesson
import com.example.benative.domain.LessonCompletionRequest
import com.example.benative.domain.LoginRequest
import com.example.benative.domain.LoginResponse
import com.example.benative.domain.RegisterRequest
import com.example.benative.domain.Task
import com.example.benative.domain.User
import com.example.benative.domain.UserStatsResponse
import io.ktor.client.call.body
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

object ApiService {

    suspend fun getLesson(token: String, lessonId: Int): Lesson = ApiModule().get("lessons/$lessonId"){
        header(HttpHeaders.Authorization, "Bearer $token")
    }.body()

    suspend fun getTasks(token: String, lessonId: Int): List<Task> = ApiModule().get("lessons/$lessonId/tasks"){
        header(HttpHeaders.Authorization, "Bearer $token")
    }.body()

    suspend fun logIn(body: LoginRequest): LoginResponse {
        val rawJson = ApiModule().post("login"){
            contentType(ContentType.Application.Json)
            setBody(body)
        }.stringOrException()

        return Json.decodeFromString(rawJson)
    }

    suspend fun register(body: RegisterRequest): LoginResponse {
        val rawJson = ApiModule().post("register") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.stringOrException()

        return Json.decodeFromString(rawJson)
    }

    suspend fun getUser(token: String): User = ApiModule().get("me"){
        header(HttpHeaders.Authorization, "Bearer $token")
        contentType(ContentType.Application.Json)
    }.body()

    suspend fun getLessons(token: String): List<Lesson> = ApiModule().get("lessons"){
        header(HttpHeaders.Authorization, "Bearer $token")
    }.body()

    suspend fun completeLesson(token: String, body: LessonCompletionRequest) {
        ApiModule().post("lessons/${body.lessonId}/complete") {
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(body)
        }
    }

    suspend fun getUserStats(token: String): UserStatsResponse = ApiModule().get("user/stats") {
        header(HttpHeaders.Authorization, "Bearer $token")
        contentType(ContentType.Application.Json)
    }.body()

    suspend fun uploadAvatar(token: String, file: File) {
        ApiModule().post("user/avatar") {
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

    suspend fun updateUserName(token: String, newName: String) {
        ApiModule().put("user/name") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(mapOf("name" to newName))
        }
    }
}
