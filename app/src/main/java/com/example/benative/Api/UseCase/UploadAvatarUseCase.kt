package com.example.benative.Api.UseCase

import com.example.benative.Api.ApiRepositoryImpl
import java.io.File

object UploadAvatarUseCase {
    suspend operator fun invoke(token: String, file: File) =
        ApiRepositoryImpl.uploadAvatar(token, file)
}