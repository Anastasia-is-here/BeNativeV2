package com.example.benative.domain.usecase

import com.example.benative.data.api.ApiRepositoryImpl
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val apiRepositoryImpl: ApiRepositoryImpl
) {
    suspend operator fun invoke(token: String) = apiRepositoryImpl.getUser(token)
}