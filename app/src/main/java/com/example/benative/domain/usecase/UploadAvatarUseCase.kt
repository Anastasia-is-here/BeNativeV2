package com.example.benative.domain.usecase

import com.example.benative.data.ApiRepositoryImpl
import java.io.File

object UploadAvatarUseCase {
    suspend operator fun invoke(token: String, file: File) =
        ApiRepositoryImpl.uploadAvatar(token, file)
}