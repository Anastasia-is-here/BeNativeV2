package com.example.benative.domain.usecase

import com.example.benative.data.api.ApiRepositoryImpl
import com.example.benative.domain.LessonCompletionRequest
import javax.inject.Inject
import javax.inject.Singleton

class CompleteLessonUseCase @Inject constructor(
    private val apiRepositoryImpl: ApiRepositoryImpl
) {
    suspend operator fun invoke(token: String, body: LessonCompletionRequest) =
        apiRepositoryImpl.completeLesson(token, body)
}