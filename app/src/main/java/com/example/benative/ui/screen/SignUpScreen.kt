package com.example.benative.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.benative.R
import com.example.benative.navigation.Screen
import com.example.benative.ui.theme.BeNativeTheme
import com.example.benative.ui.theme.MajorMonoDisplay
import com.example.benative.ui.theme.ManropeBold


@Composable
fun SignUpScreen(onNavigateTo: (Screen) -> Unit = {}) {
    var loginOrEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_clouds_4),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "create an account",
                style = TextStyle(
                    fontSize = 30.sp,
                    color = Color.White,
                    fontFamily = MajorMonoDisplay
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            BasicTextField(
                value = loginOrEmail,
                onValueChange = { loginOrEmail = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x80FFFFFF), RoundedCornerShape(20.dp))
                    .padding(12.dp),
                textStyle = TextStyle(
                    color = Color.White,
                    fontFamily = MajorMonoDisplay
                ),
                decorationBox = { innerTextField ->
                    if (loginOrEmail.isEmpty()) {
                        Text(
                            text = "email",
                            color = Color.White,
                            fontFamily = MajorMonoDisplay
                        )
                    }
                    innerTextField()
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            BasicTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x80FFFFFF), RoundedCornerShape(20.dp))
                    .padding(12.dp),
                textStyle = TextStyle(
                    color = Color.White,
                    fontFamily = MajorMonoDisplay
                ),
                visualTransformation = PasswordVisualTransformation(),
                decorationBox = { innerTextField ->
                    if (password.isEmpty()) {
                        Text(
                            text = "password",
                            color = Color.White,
                            fontFamily = MajorMonoDisplay
                        )
                    }
                    innerTextField()
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            BasicTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x80FFFFFF), RoundedCornerShape(20.dp))
                    .padding(12.dp),
                textStyle = TextStyle(
                    color = Color.White,
                    fontFamily = MajorMonoDisplay
                ),
                visualTransformation = PasswordVisualTransformation(),
                decorationBox = { innerTextField ->
                    if (confirmPassword.isEmpty()) {
                        Text(
                            text = "confirm password",
                            color = Color.White,
                            fontFamily = MajorMonoDisplay
                        )
                    }
                    innerTextField()
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Логика регистрации
                    // navController.navigate("main") {
                    //    popUpTo("sign_up") { inclusive = true }
                    // }
                },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.width(120.dp)
            ) {
                Text(
                    text = "GO",
                    color = Color.White,
                    fontFamily = ManropeBold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    BeNativeTheme {
        SignUpScreen()
    }
}