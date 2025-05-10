package com.example.benative.domain.usecase

import com.example.benative.data.ApiRepositoryImpl
import com.example.benative.data.KtorHttpException
import com.example.benative.domain.LoginRequest
import com.example.benative.domain.LoginResponse

object LogInUseCase {
    suspend operator fun invoke(body: LoginRequest): Result<LoginResponse> {
        return try {
            val response = ApiRepositoryImpl.logIn(body)
            Result.success(response)
        } catch (e: KtorHttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
