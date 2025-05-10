package com.example.benative.domain.usecase

import com.example.benative.data.ApiRepositoryImpl
import com.example.benative.data.KtorHttpException
import com.example.benative.domain.LoginResponse
import com.example.benative.domain.RegisterRequest

object RegisterUseCase {
    suspend operator fun invoke(body: RegisterRequest): Result<LoginResponse> {
        return try {
            val response = ApiRepositoryImpl.register(body)
            Result.success(response)
        } catch (e: KtorHttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}