package com.example.benative.domain.usecase

import com.example.benative.data.api.ApiRepositoryImpl
import java.io.File
import javax.inject.Inject

class UploadAvatarUseCase @Inject constructor(
    private val apiRepositoryImpl: ApiRepositoryImpl
) {
    suspend operator fun invoke(token: String, file: File) =
        apiRepositoryImpl.uploadAvatar(token, file)
}