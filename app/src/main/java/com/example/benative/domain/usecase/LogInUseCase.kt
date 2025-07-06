package com.example.benative.domain.usecase

import com.example.benative.data.api.ApiRepositoryImpl
import com.example.benative.data.KtorHttpException
import com.example.benative.domain.LoginRequest
import com.example.benative.domain.LoginResponse
import javax.inject.Inject
import javax.inject.Singleton

class LogInUseCase @Inject constructor(
    private val apiRepositoryImpl: ApiRepositoryImpl
) {
    suspend operator fun invoke(body: LoginRequest): Result<LoginResponse> {
        return try {
            val response = apiRepositoryImpl.logIn(body)
            Result.success(response)
        } catch (e: KtorHttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
