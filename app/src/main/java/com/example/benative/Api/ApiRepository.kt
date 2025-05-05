package com.example.benative.Api

import com.example.benative.server.Lesson
import com.example.benative.server.LessonCompletionRequest
import com.example.benative.server.LoginRequest
import com.example.benative.server.LoginResponse
import com.example.benative.server.Task
import com.example.benative.server.User
import io.ktor.client.statement.HttpResponse

interface ApiRepository{

    suspend fun getLesson(token: String, lessonId: Int): Lesson

    suspend fun getTasks(token: String, lessonId: Int): List<Task>

    suspend fun logIn(body: LoginRequest): LoginResponse

    suspend fun getUser(token: String): User

    suspend fun getLessons(token: String): List<Lesson>

    suspend fun completeLesson(token: String, body: LessonCompletionRequest)
}