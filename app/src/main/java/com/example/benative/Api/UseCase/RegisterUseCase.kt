package com.example.benative.Api.UseCase

import com.example.benative.Api.ApiRepositoryImpl
import com.example.benative.server.RegisterRequest

object RegisterUseCase {
    suspend operator fun invoke(body: RegisterRequest) = ApiRepositoryImpl.register(body)
}