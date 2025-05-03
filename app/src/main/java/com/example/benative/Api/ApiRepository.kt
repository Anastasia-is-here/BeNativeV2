package com.example.benative.Api

import com.example.benative.server.Lesson
import com.example.benative.server.LoginRequest
import com.example.benative.server.TaskResponse
import com.example.benative.server.UserResponse
import io.ktor.client.statement.HttpResponse

interface ApiRepository{

    suspend fun getLesson(token: String, lessonId: Int): Lesson

    suspend fun getTasks(token: String, lessonId: Int): List<TaskResponse>

    suspend fun logIn(body: LoginRequest): HttpResponse

    suspend fun getUser(token: String): UserResponse
}