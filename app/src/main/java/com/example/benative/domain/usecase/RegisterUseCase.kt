package com.example.benative.domain.usecase

import com.example.benative.data.ApiRepositoryImpl
import com.example.benative.domain.RegisterRequest

object RegisterUseCase {
    suspend operator fun invoke(body: RegisterRequest) = ApiRepositoryImpl.register(body)
}