package com.example.benative.domain.usecase

import com.example.benative.data.api.ApiRepositoryImpl
import javax.inject.Inject

class GetUserStatsUseCase @Inject constructor(
    private val apiRepositoryImpl: ApiRepositoryImpl
) {
    suspend operator fun invoke(token: String) = apiRepositoryImpl.getUserStats(token)
}