package com.example.benative.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.benative.R
import com.example.benative.navigation.Screen
import com.example.benative.ui.theme.BeNativeTheme
import com.example.benative.ui.theme.MajorMonoDisplay

@Composable
fun SplashScreen(onNavigateTo: (Screen) -> Unit = {}) {
    LaunchedEffect(Unit) {
            onNavigateTo(Screen.SignInScreen)
        }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC))
    ) {

            Image(
                painter = painterResource(id = R.drawable.background_clouds_1),
                contentDescription = null,
                modifier = Modifier.height(300.dp).width(300.dp).align(Alignment.TopEnd)
            )

            Image(
                painter = painterResource(id = R.drawable.background_clouds_2),
                contentDescription = null,
                modifier = Modifier.size(600.dp).align(Alignment.BottomStart)
            )

            Text(
                text = "BeNative",
                fontSize = 40.sp,
                color = Color.White,
                fontFamily = MajorMonoDisplay,
                modifier = Modifier.align(Alignment.Center)
            )



    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    BeNativeTheme {
        //SplashScreen()
    }
}