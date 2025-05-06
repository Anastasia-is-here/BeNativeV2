package com.example.benative.domain.usecase

import com.example.benative.data.ApiRepositoryImpl

object GetUserStatsUseCase {
    suspend operator fun invoke(token: String) = ApiRepositoryImpl.getUserStats(token)
}