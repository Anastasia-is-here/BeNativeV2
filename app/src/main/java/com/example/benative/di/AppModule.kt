package com.example.benative.di

import android.content.Context
import com.example.benative.data.AuthManager
import com.example.benative.data.api.ApiRepositoryImpl
import com.example.benative.data.api.ApiService
import com.example.benative.domain.usecase.CompleteLessonUseCase
import com.example.benative.domain.usecase.DeleteAvatarUseCase
import com.example.benative.domain.usecase.GetLessonUseCase
import com.example.benative.domain.usecase.GetLessonsUseCase
import com.example.benative.domain.usecase.GetTasksUseCase
import com.example.benative.domain.usecase.GetUserStatsUseCase
import com.example.benative.domain.usecase.GetUserUseCase
import com.example.benative.domain.usecase.LogInUseCase
import com.example.benative.domain.usecase.RegisterUseCase
import com.example.benative.domain.usecase.UpdateUserNameUseCase
import com.example.benative.domain.usecase.UploadAvatarUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient{

        val client = HttpClient(CIO) {
            expectSuccess = false
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTP
                    host = "10.0.2.2"
                    port = 8080

                }
                contentType(ContentType.Application.Json)
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = false
                    encodeDefaults = true
                    explicitNulls = true
                })
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 15000
                connectTimeoutMillis = 10000
                socketTimeoutMillis = 15000
            }
        }

        return client

    }

    @Provides
    @Singleton
    fun provideApiService(client: HttpClient): ApiService {
        return ApiService(client)
    }

    @Provides
    @Singleton
    fun provideApiRepositoryImpl(apiService: ApiService): ApiRepositoryImpl {
        return ApiRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideCompleteLessonUseCase(apiRepositoryImpl: ApiRepositoryImpl): CompleteLessonUseCase {
        return CompleteLessonUseCase(apiRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideDeleteAvatarUseCase(apiRepositoryImpl: ApiRepositoryImpl): DeleteAvatarUseCase {
        return DeleteAvatarUseCase(apiRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideGetLessonsUseCase(apiRepositoryImpl: ApiRepositoryImpl): GetLessonsUseCase {
        return GetLessonsUseCase(apiRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideGetLessonUseCase(apiRepositoryImpl: ApiRepositoryImpl): GetLessonUseCase {
        return GetLessonUseCase(apiRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideGetTasksUseCase(apiRepositoryImpl: ApiRepositoryImpl): GetTasksUseCase {
        return GetTasksUseCase(apiRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideGetUserStatsUseCase(apiRepositoryImpl: ApiRepositoryImpl): GetUserStatsUseCase {
        return GetUserStatsUseCase(apiRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideGetUserUseCase(apiRepositoryImpl: ApiRepositoryImpl): GetUserUseCase {
        return GetUserUseCase(apiRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(apiRepositoryImpl: ApiRepositoryImpl): LogInUseCase {
        return LogInUseCase(apiRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(apiRepositoryImpl: ApiRepositoryImpl): RegisterUseCase {
        return RegisterUseCase(apiRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideUpdateUserNameUseCase(apiRepositoryImpl: ApiRepositoryImpl): UpdateUserNameUseCase {
        return UpdateUserNameUseCase(apiRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideUploadAvatarUseCase(apiRepositoryImpl: ApiRepositoryImpl): UploadAvatarUseCase {
        return UploadAvatarUseCase(apiRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideAuthManager(@ApplicationContext context: Context): AuthManager {
        return AuthManager(context)
    }
}