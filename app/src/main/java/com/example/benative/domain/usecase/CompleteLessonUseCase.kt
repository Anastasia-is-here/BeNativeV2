package com.example.benative.domain.usecase

import com.example.benative.data.ApiRepositoryImpl
import com.example.benative.domain.LessonCompletionRequest

object CompleteLessonUseCase {
    suspend operator fun invoke(token: String, body: LessonCompletionRequest) =
        ApiRepositoryImpl.completeLesson(token, body)
}