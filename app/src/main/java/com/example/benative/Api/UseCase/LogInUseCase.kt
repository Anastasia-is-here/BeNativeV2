package com.example.benative.Api.UseCase

import com.example.benative.Api.ApiRepositoryImpl
import com.example.benative.server.LoginRequest

object LogInUseCase {
    suspend operator fun invoke(body: LoginRequest) = ApiRepositoryImpl.logIn(body)
}
