package com.example.benative.data

import com.example.benative.domain.Lesson
import com.example.benative.domain.LessonCompletionRequest
import com.example.benative.domain.LoginRequest
import com.example.benative.domain.LoginResponse
import com.example.benative.domain.RegisterRequest
import com.example.benative.domain.Task
import com.example.benative.domain.User
import com.example.benative.domain.UserStatsResponse
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

    suspend fun updateUserName(token: String, newName: String)
}