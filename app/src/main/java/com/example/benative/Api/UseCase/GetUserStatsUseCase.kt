package com.example.benative.Api.UseCase

import com.example.benative.Api.ApiRepositoryImpl

object GetUserStatsUseCase {
    suspend operator fun invoke(token: String) = ApiRepositoryImpl.getUserStats(token)
}