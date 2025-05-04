package com.example.benative.Api.UseCase

import com.example.benative.Api.ApiRepositoryImpl

object GetTasksUseCase {
    suspend operator fun invoke(token: String, lessonId: Int) = ApiRepositoryImpl.getTasks(token, lessonId)
}