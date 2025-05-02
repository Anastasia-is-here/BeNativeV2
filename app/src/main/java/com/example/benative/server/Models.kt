package com.example.benative.server

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String
)

@Serializable
data class LoginRequest(
    val login: String,
    val password: String
)

@Serializable
data class ErrorResponse(
    val error: String
)

@Serializable
data class UserResponse(
    val id: Int,
    val name: String,
    val login: String,
    val experience: Int
)

@Serializable
data class Lesson(
    val id: Int,
    val title: String,
    val iconUrl: String
)
