package com.example.benative.presentation.screen.state

import com.example.benative.domain.LoginResponse

sealed class SignInScreenEvent{
    data class LoginUpdated(val newLogin: String): SignInScreenEvent()
    data class PasswordUpdated(val newPassword: String): SignInScreenEvent()
    data object SignInBtnClick: SignInScreenEvent()
}

data class SignInScreenState(
    val login: String = "",
    val password: String = "",
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val loginResult: Result<LoginResponse>? = null
)
