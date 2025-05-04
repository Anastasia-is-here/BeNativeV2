package com.example.benative.Api.UseCase

import com.example.benative.Api.ApiRepositoryImpl
import com.example.benative.server.LessonCompletionRequest

object CompleteLessonUseCase {
    suspend operator fun invoke(token: String, body: LessonCompletionRequest) = ApiRepositoryImpl.completeLesson(token, body)
}