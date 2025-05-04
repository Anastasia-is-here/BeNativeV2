package com.example.benative.Api.UseCase

import com.example.benative.Api.ApiRepositoryImpl

object GetUserUseCase {
    suspend operator fun invoke(token: String) = ApiRepositoryImpl.getUser(token)
}