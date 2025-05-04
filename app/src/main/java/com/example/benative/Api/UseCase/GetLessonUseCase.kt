package com.example.benative.Api.UseCase

import com.example.benative.Api.ApiRepositoryImpl

object GetLessonUseCase {
    suspend operator fun invoke(token: String, lessonId: Int) = ApiRepositoryImpl.getLesson(token, lessonId)
}