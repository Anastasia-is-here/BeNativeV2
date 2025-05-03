package com.example.benative.Api

import com.example.benative.server.Lesson
import com.example.benative.server.LoginRequest
import com.example.benative.server.TaskResponse
import com.example.benative.server.UserResponse
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

class ApiRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ApiRepository {

    override suspend fun getLesson(token: String, lessonId: Int): Lesson{
        return apiService.getLesson(token, lessonId)
    }

    override suspend fun getTasks(token: String, lessonId: Int): List<TaskResponse>{
        return apiService.getTasks(token, lessonId)
    }

    override suspend fun logIn(body: LoginRequest): HttpResponse{
        return apiService.logIn(body)
    }

    override suspend fun getUser(token: String): UserResponse{
        return apiService.getUser(token)
    }
}