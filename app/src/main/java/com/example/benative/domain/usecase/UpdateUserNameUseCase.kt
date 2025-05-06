package com.example.benative.domain.usecase

import com.example.benative.data.ApiRepositoryImpl

object UpdateUserNameUseCase{
    suspend operator fun invoke(token: String, newName: String) =
        ApiRepositoryImpl.updateUserName(token, newName)
}