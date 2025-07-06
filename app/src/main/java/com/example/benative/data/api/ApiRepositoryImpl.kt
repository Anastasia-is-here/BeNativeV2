package com.example.benative.data.api

import com.example.benative.domain.Lesson
import com.example.benative.domain.LessonCompletionRequest
import com.example.benative.domain.LoginRequest
import com.example.benative.domain.LoginResponse
import com.example.benative.domain.RegisterRequest
import com.example.benative.domain.Task
import com.example.benative.domain.User
import com.example.benative.domain.UserStatsResponse
import java.io.File
import javax.inject.Inject

class ApiRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ApiRepository {

    override suspend fun getLesson(token: String, lessonId: Int): Lesson {
        return apiService.getLesson(token, lessonId)
    }

    override suspend fun getTasks(token: String, lessonId: Int): List<Task>{
        return apiService.getTasks(token, lessonId)
    }

    override suspend fun logIn(body: LoginRequest): LoginResponse {
        return apiService.logIn(body)
    }

    override suspend fun register(body: RegisterRequest): LoginResponse {
        return apiService.register(body)
    }

    override suspend fun getUser(token: String): User {
        return apiService.getUser(token)
    }

    override suspend fun getLessons(token: String): List<Lesson>{
        return apiService.getLessons(token)
    }

    override suspend fun completeLesson(token: String, body: LessonCompletionRequest) {
        return apiService.completeLesson(token, body)
    }

    override suspend fun getUserStats(token: String): UserStatsResponse {
        return apiService.getUserStats(token)
    }

    override suspend fun uploadAvatar(token: String, file: File) {
        return apiService.uploadAvatar(token, file)
    }

    override suspend fun deleteAvatar(token: String) {
        return apiService.deleteAvatar(token)
    }

    override suspend fun updateUserName(token: String, newName: String) {
        return apiService.updateUserName(token, newName)
    }
}