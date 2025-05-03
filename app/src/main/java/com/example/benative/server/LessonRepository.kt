package com.example.benative.server

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.example.benative.Api.ApiClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.headers

object LessonRepository {
    @OptIn(UnstableApi::class)
    suspend fun fetchLessonById(token: String, lessonId: Int): Lesson {

        return ApiClient.client.get("${ApiClient.lessonsUrl}/$lessonId") {
            headers {
                append("Authorization", "Bearer $token")
            }
        }.body()

    }


    suspend fun fetchTasksForLesson(token: String, lessonId: Int): List<TaskResponse> {
        return ApiClient.client.get(ApiClient.tasksUrl(lessonId)) {
            headers {
                append("Authorization", "Bearer $token")
            }
        }.body()
    }
}
