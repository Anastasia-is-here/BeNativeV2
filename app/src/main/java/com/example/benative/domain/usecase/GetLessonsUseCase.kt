package com.example.benative.domain.usecase

import com.example.benative.data.ApiRepositoryImpl

object GetLessonsUseCase {
    suspend operator fun invoke(token: String) = ApiRepositoryImpl.getLessons(token)
}