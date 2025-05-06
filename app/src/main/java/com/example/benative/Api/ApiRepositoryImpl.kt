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

object ApiRepositoryImpl : ApiRepository {

    override suspend fun getLesson(token: String, lessonId: Int): Lesson {
        return ApiService.getLesson(token, lessonId)
    }

    override suspend fun getTasks(token: String, lessonId: Int): List<Task>{
        return ApiService.getTasks(token, lessonId)
    }

    override suspend fun logIn(body: LoginRequest): LoginResponse{
        return ApiService.logIn(body)
    }

    override suspend fun register(body: RegisterRequest): LoginResponse{
        return ApiService.register(body)
    }

    override suspend fun getUser(token: String): User{
        return ApiService.getUser(token)
    }

    override suspend fun getLessons(token: String): List<Lesson>{
        return ApiService.getLessons(token)
    }

    override suspend fun completeLesson(token: String, body: LessonCompletionRequest) {
        return ApiService.completeLesson(token, body)
    }

    override suspend fun getUserStats(token: String): UserStatsResponse {
        return ApiService.getUserStats(token)
    }

    override suspend fun uploadAvatar(token: String, file: File) {
        return ApiService.uploadAvatar(token, file)
    }
}