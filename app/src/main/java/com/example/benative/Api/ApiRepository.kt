package com.example.benative.Api

import com.example.benative.server.Lesson
import com.example.benative.server.LessonCompletionRequest
import com.example.benative.server.LoginRequest
import com.example.benative.server.LoginResponse
import com.example.benative.server.RegisterRequest
import com.example.benative.server.Task
import com.example.benative.server.User
import com.example.benative.server.UserStatsResponse
import java.io.File

interface ApiRepository{

    suspend fun getLesson(token: String, lessonId: Int): Lesson

    suspend fun getTasks(token: String, lessonId: Int): List<Task>

    suspend fun logIn(body: LoginRequest): LoginResponse

    suspend fun register(body: RegisterRequest): LoginResponse

    suspend fun getUser(token: String): User

    suspend fun getLessons(token: String): List<Lesson>

    suspend fun completeLesson(token: String, body: LessonCompletionRequest)

    suspend fun getUserStats(token: String): UserStatsResponse

    suspend fun uploadAvatar(token: String, file: File)
}