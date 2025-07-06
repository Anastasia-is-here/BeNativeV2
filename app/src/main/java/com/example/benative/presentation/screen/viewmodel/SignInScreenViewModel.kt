package com.example.benative.presentation.screen.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.benative.data.AuthManager
import com.example.benative.data.KtorHttpException
import com.example.benative.domain.LoginRequest
import com.example.benative.domain.usecase.LogInUseCase
import com.example.benative.presentation.screen.state.SignInScreenEvent
import com.example.benative.presentation.screen.state.SignInScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.State

@HiltViewModel
class SignInScreenViewModel @Inject constructor(
    private val loginUseCase: LogInUseCase,
    private val authManager: AuthManager
) : ViewModel() {

    private val _state = mutableStateOf(SignInScreenState())
    val state: State<SignInScreenState> = _state

    fun onEvent(event: SignInScreenEvent){

        Log.e("EVENTVALUE", event.toString())
        when (event){
            is SignInScreenEvent.LoginUpdated -> {
                Log.e("LOGINUPD", "Works")
                this._state.value = _state.value.copy(login = event.newLogin)
            }
            is SignInScreenEvent.PasswordUpdated -> {
                this._state.value = _state.value.copy(password = event.newPassword)
            }
            SignInScreenEvent.SignInBtnClick -> signIn()
        }
    }
    private fun signIn() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            if (_state.value.login.isEmpty() || _state.value.password.isEmpty()) {
                _state.value = _state.value.copy(errorMessage = "All fields are required", isLoading = false)
                return@launch
            }
            if (_state.value.password.length > 72) {
                _state.value = _state.value.copy(errorMessage = "Password must be 72 characters or less", isLoading = false)
                return@launch
            }

            try {
                val request = LoginRequest(_state.value.login, _state.value.password)
                val result = loginUseCase(request)

                result.fold(
                    onSuccess = { response ->
                        authManager.saveToken(response.token)
                        _state.value = _state.value.copy(isLoading = false, loginResult = result)
                    },
                    onFailure = { error ->
                        val _errorMessage = when (error) {
                            is KtorHttpException -> {
                                if (error.code == 401) {
                                    "Invalid login or password"
                                } else {
                                    "Error ${error.code}: ${error.message}"
                                }
                            }

                            else -> {
                                "Something went wrong: ${error.localizedMessage}"
                            }
                        }
                        _state.value = _state.value.copy(isLoading = false, errorMessage = _errorMessage)
                    }
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, errorMessage = "Error: ${e.message}")
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }
}