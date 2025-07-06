package com.example.benative.domain.usecase

import com.example.benative.data.api.ApiRepositoryImpl
import javax.inject.Inject

class UpdateUserNameUseCase @Inject constructor(
    private val apiRepositoryImpl: ApiRepositoryImpl
) {
    suspend operator fun invoke(token: String, newName: String) =
        apiRepositoryImpl.updateUserName(token, newName)
}