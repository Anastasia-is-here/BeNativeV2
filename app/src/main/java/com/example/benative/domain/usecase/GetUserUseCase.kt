package com.example.benative.domain.usecase

import com.example.benative.data.ApiRepositoryImpl

object GetUserUseCase {
    suspend operator fun invoke(token: String) = ApiRepositoryImpl.getUser(token)
}