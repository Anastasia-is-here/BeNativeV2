package com.example.benative.Api.UseCase

import com.example.benative.Api.ApiRepositoryImpl

object GetLessonsUseCase {
    suspend operator fun invoke(token: String) = ApiRepositoryImpl.getLessons(token)
}