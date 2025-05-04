package com.example.benative.Api

import com.example.benative.server.Lesson
import com.example.benative.server.LessonCompletionRequest
import com.example.benative.server.LoginRequest
import com.example.benative.server.LoginResponse
import com.example.benative.server.Task
import com.example.benative.server.TaskResultDto
import com.example.benative.server.User
import io.ktor.client.statement.HttpResponse

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

    override suspend fun getUser(token: String): User{
        return ApiService.getUser(token)
    }

    override suspend fun getLessons(token: String): List<Lesson>{
        return ApiService.getLessons(token)
    }

    override suspend fun completeLesson(
        token: String,
        body: LessonCompletionRequest
    ): List<TaskResultDto> {
        return ApiService.completeLesson(token, body)
    }
}