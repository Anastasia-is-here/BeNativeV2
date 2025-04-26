package com.example.benative.server

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val login: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val token: String
)

@Serializable
data class ErrorResponse(
    val error: String
)