package com.example.benative.domain.usecase

import com.example.benative.data.api.ApiRepositoryImpl
import javax.inject.Inject
import javax.inject.Singleton

class DeleteAvatarUseCase @Inject constructor(
    private val apiRepositoryImpl: ApiRepositoryImpl
) {
    suspend operator fun invoke(token: String) = apiRepositoryImpl.deleteAvatar(token)
}