package com.example.benative.domain.usecase

import com.example.benative.data.api.ApiRepositoryImpl
import javax.inject.Inject

class GetLessonUseCase @Inject constructor(
    private val apiRepositoryImpl: ApiRepositoryImpl
) {
    suspend operator fun invoke(token: String, lessonId: Int) = apiRepositoryImpl.getLesson(token, lessonId)
}