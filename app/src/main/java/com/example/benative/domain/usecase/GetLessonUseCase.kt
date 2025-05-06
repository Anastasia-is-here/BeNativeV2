package com.example.benative.domain.usecase

import com.example.benative.data.ApiRepositoryImpl

object GetLessonUseCase {
    suspend operator fun invoke(token: String, lessonId: Int) = ApiRepositoryImpl.getLesson(token, lessonId)
}