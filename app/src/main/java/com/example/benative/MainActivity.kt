package com.example.benative

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.benative.presentation.navigation.Navigation
import com.example.benative.presentation.theme.BeNativeTheme
import dagger.hilt.android.AndroidEntryPoint

data class RegisterRequest(val name: String, val login: String, val password: String)
data class LoginRequest(val login: String, val password: String)
data class LoginResponse(val token: String)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            BeNativeTheme {
                val navController = rememberNavController()
                Navigation(navController)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainNavigationPreview() {
    BeNativeTheme {

    }
}