package com.example.benative.domain.usecase

import com.example.benative.data.ApiRepositoryImpl
import com.example.benative.domain.LoginRequest

object LogInUseCase {
    suspend operator fun invoke(body: LoginRequest) = ApiRepositoryImpl.logIn(body)
}
