package com.example.benative.server

import androidx.annotation.OptIn
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.headers

object LessonRepository {
    @OptIn(UnstableApi::class)
    suspend fun fetchLessonById(token: String, lessonId: Int): Lesson {


        Log.d("URLDEBUG", ApiClient.lessonsUrl + "/" + lessonId.toString())
        val response = ApiClient.client.get("${ApiClient.lessonsUrl}/$lessonId") {
            headers {
                append("Authorization", "Bearer $token")
            }
        }
        val rawJson = response.bodyAsText()
        Log.d("TokenDebug",token )
        Log.d("LessonRawResponse",rawJson )
        Log.d("StatusDebug", response.status.toString())
        return response.body()
    }


    suspend fun fetchTasksForLesson(token: String, lessonId: Int): List<TaskResponse> {
        return ApiClient.client.get(ApiClient.tasksUrl(lessonId)) {
            headers {
                append("Authorization", "Bearer $token")
            }
        }.body()
    }
}
