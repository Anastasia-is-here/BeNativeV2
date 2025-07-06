package com.example.benative.presentation.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.benative.R
import com.example.benative.data.AuthManager
import com.example.benative.data.KtorHttpException
import com.example.benative.domain.LoginRequest
import com.example.benative.domain.usecase.LogInUseCase
import com.example.benative.presentation.component.StyledOutlinedTextField
import com.example.benative.presentation.component.button.ButtonPrimary
import com.example.benative.presentation.navigation.Screen
import com.example.benative.presentation.screen.state.SignInScreenEvent
import com.example.benative.presentation.screen.state.SignInScreenState
import com.example.benative.presentation.screen.viewmodel.SignInScreenViewModel
import com.example.benative.presentation.theme.BeNativeTheme
import com.example.benative.presentation.theme.MajorMonoDisplay
import com.example.benative.presentation.theme.ManropeBold
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    onNavigateTo: (Screen) -> Unit
){
    val viewModel = hiltViewModel<SignInScreenViewModel>()

    LaunchedEffect(viewModel.state.value.loginResult) {
        viewModel.state.value.loginResult?.fold(
            onSuccess = {
                onNavigateTo(Screen.MainScreen)
            },
            onFailure = {

            }
        )
    }

    SignInView(
        state = viewModel.state.value,
        onEvent = viewModel::onEvent,
        onNavigateTo = onNavigateTo
    )
}

@Composable
fun SignInView(
    onNavigateTo: (Screen) -> Unit = {},
    state: SignInScreenState = SignInScreenState(),
    onEvent: (SignInScreenEvent) -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxSize().background(Color(0xFFB3E5FC))
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_clouds_5),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopStart
        )

        Text(
            text = "sign in",
            fontSize = 30.sp,
            color = Color.White,
            fontFamily = MajorMonoDisplay
        )

        Spacer(modifier = Modifier.size(30.dp))

        StyledOutlinedTextField(
            value = state.login,
            onValueChange = {
                Log.e("LOGINEVENT", "Works")
                onEvent(SignInScreenEvent.LoginUpdated(it))
            },
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
            }
        )
        Log.e("VALUE", state.login)

        Spacer(modifier = Modifier.height(16.dp))

        StyledOutlinedTextField(
            value = state.password,
            onValueChange = {
                onEvent(SignInScreenEvent.PasswordUpdated(it))
            },
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
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (state.isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
        } else {
            ButtonPrimary(
                onClick = { onEvent(SignInScreenEvent.SignInBtnClick) },
                modifier = Modifier.width(120.dp)
            ) {
                Text(
                    text = "Go",
                    color = Color.White,
                    fontFamily = ManropeBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            state.errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            TextButton(onClick = {
                onNavigateTo(Screen.SignUpScreen)
            }) {
                Text(
                    text = "Don't have an account? Sign up",
                    color = Color.White,
                    fontFamily = FontFamily.SansSerif,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
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
fun SignInScreenPreview() {
    BeNativeTheme {
        SignInView()
    }
}