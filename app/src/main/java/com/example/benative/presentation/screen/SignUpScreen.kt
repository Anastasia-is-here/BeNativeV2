package com.example.benative.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.benative.domain.usecase.RegisterUseCase
import com.example.benative.R
import com.example.benative.domain.RegisterRequest
import com.example.benative.presentation.navigation.Screen
import com.example.benative.data.AuthManager
import com.example.benative.data.KtorHttpException
import com.example.benative.presentation.theme.BeNativeTheme
import com.example.benative.presentation.theme.MajorMonoDisplay
import com.example.benative.presentation.theme.ManropeBold
import kotlinx.coroutines.launch


@Composable
fun SignUpScreen(onNavigateBack: () -> Unit,
                 onNavigateTo: (Screen) -> Unit = {}) {
    var login by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Box(modifier = Modifier.background(Color(0xFFB3E5FC)).fillMaxSize()) {
        Box(
            modifier = Modifier
                .padding(16.dp, 30.dp, 16.dp, 16.dp)
                .align(Alignment.TopStart)
                .size(55.dp)
                .clickable(onClick = onNavigateBack)
        ) {
            Icon(
                painter = painterResource(R.drawable.backarrow_icon),
                contentDescription = "Back",
                tint = Color(0xFFE91E63),
                modifier = Modifier.fillMaxSize()
            )
        }


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {

            Image(
                painter = painterResource(id = R.drawable.background_clouds_5),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopStart
            )

            Text(
                text = "create an",
                fontSize = 30.sp,
                color = Color.White,
                fontFamily = MajorMonoDisplay
            )
            Text(
                text = "account",
                fontSize = 30.sp,
                color = Color.White,
                fontFamily = MajorMonoDisplay
            )

            Spacer(modifier = Modifier.size(14.dp))

            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                modifier = Modifier
                    .width(300.dp)
                    .background(Color("#F2F2F2".toColorInt()), RoundedCornerShape(30.dp)),
                placeholder = {
                    Text(
                        text = "login",
                        fontFamily = MajorMonoDisplay,
                        color = Color(0xFF636363),
                        fontSize = 18.sp
                    )
                },
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    errorTextColor = Color.Red,
                    focusedBorderColor = Color("#FFFFFF".toColorInt()),

                    )
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier
                    .width(300.dp)
                    .background(Color("#F2F2F2".toColorInt()), RoundedCornerShape(30.dp)),
                placeholder = {
                    Text(
                        text = "username",
                        fontFamily = MajorMonoDisplay,
                        color = Color(0xFF636363),
                        fontSize = 18.sp
                    )
                },
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    errorTextColor = Color.Red,
                    focusedBorderColor = Color("#FFFFFF".toColorInt()),

                    )

            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .width(300.dp)
                    .background(Color("#F2F2F2".toColorInt()), RoundedCornerShape(30.dp)),
                placeholder = {
                    Text(
                        text = "password",
                        fontFamily = MajorMonoDisplay,
                        color = Color(0xFF636363),
                        fontSize = 18.sp
                    )
                },
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    errorTextColor = Color.Red,
                    focusedBorderColor = Color("#FFFFFF".toColorInt())
                )
            )
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                modifier = Modifier
                    .width(300.dp)
                    .background(Color("#F2F2F2".toColorInt()), RoundedCornerShape(30.dp)),
                placeholder = {
                    Text(
                        text = "confirm password",
                        fontFamily = MajorMonoDisplay,
                        color = Color(0xFF636363),
                        fontSize = 18.sp
                    )
                },
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    errorTextColor = Color.Red,
                    focusedBorderColor = Color("#FFFFFF".toColorInt())
                )
            )

            Spacer(modifier = Modifier.height(8.dp))


            Button(
                onClick = {
                    errorMessage = null

                    // Проверка: поля не пустые
                    if (login.isBlank() || password.isBlank() || confirmPassword.isBlank() || name.isBlank()) {
                        errorMessage = "All fields are required"
                        return@Button
                    }

                    // Проверка: латиница
                    val latinRegex = Regex("^[a-zA-Z0-9]+$")
                    if (!latinRegex.matches(login) || !latinRegex.matches(password)) {
                        errorMessage = "Only Latin letters and numbers allowed"
                        return@Button
                    }

                    // Проверка: длина пароля
                    if (password.length < 8) {
                        errorMessage = "Password must be at least 8 characters"
                        return@Button
                    }

                    // Проверка: хотя бы одна цифра
                    if (!password.any { it.isDigit() }) {
                        errorMessage = "Password must contain at least one number"
                        return@Button
                    }

                    // Проверка: совпадение пароля
                    if (password != confirmPassword) {
                        errorMessage = "Passwords do not match"
                        return@Button
                    }

                    isLoading = true

                    coroutineScope.launch {

                            val request = RegisterRequest(
                                login = login,
                                password = password,
                                name = name
                            )
                            val registerResponse = RegisterUseCase(request)

                        registerResponse.onSuccess { response ->
                            AuthManager.saveToken(context, response.token)
                            // Переходим на главный экран
                            onNavigateTo(Screen.MainScreen)
                        }.onFailure { error ->
                            when (error) {
                                is KtorHttpException -> {
                                    if (error.code == 409) {
                                        errorMessage = "User with this login already exists"
                                    } else {
                                        errorMessage = "Error ${error.code}: ${error.message}"
                                    }
                                }
                                else -> {
                                    errorMessage = "Something went wrong: ${error.localizedMessage}"
                                }
                            }
                        }

                    }
                },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.width(120.dp)
            ) {
                Text(
                    text = "Go",
                    color = Color.White,
                    fontFamily = ManropeBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "Small phone",
    widthDp = 360,
    heightDp = 800
)
@Preview(
    showBackground = true,
    name = "medium phone",
    widthDp = 390,
    heightDp = 844
)
@Preview(
    showBackground = true,
    name = "large phone",
    widthDp = 393,
    heightDp = 873
)
@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    BeNativeTheme {
        SignUpScreen(onNavigateBack = {})
    }
}