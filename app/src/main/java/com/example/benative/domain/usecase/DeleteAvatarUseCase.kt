package com.example.benative.domain.usecase

import com.example.benative.data.ApiRepositoryImpl

object DeleteAvatarUseCase {
    suspend operator fun invoke(token: String) = ApiRepositoryImpl.deleteAvatar(token)
}