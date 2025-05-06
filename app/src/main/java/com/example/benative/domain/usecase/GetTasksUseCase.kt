package com.example.benative.domain.usecase

import com.example.benative.data.ApiRepositoryImpl

object GetTasksUseCase {
    suspend operator fun invoke(token: String, lessonId: Int) = ApiRepositoryImpl.getTasks(token, lessonId)
}