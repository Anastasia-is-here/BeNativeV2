package com.example.benative.domain.usecase

import com.example.benative.data.api.ApiRepositoryImpl
import com.example.benative.data.KtorHttpException
import com.example.benative.domain.LoginResponse
import com.example.benative.domain.RegisterRequest
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val apiRepositoryImpl: ApiRepositoryImpl
) {
    suspend operator fun invoke(body: RegisterRequest): Result<LoginResponse> {
        return try {
            val response = apiRepositoryImpl.register(body)
            Result.success(response)
        } catch (e: KtorHttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}