package com.example.benative.presentation.screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.benative.domain.usecase.GetUserStatsUseCase
import com.example.benative.R
import com.example.benative.data.AuthManager
import com.example.benative.domain.UserStatsResponse
import com.example.benative.presentation.theme.BeNativeTheme
import com.example.benative.presentation.theme.MajorMonoDisplay
import com.example.benative.presentation.theme.ManropeBold
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun CreditsScreen(onNavigateBack: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC))
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp, 30.dp, 16.dp, 16.dp)
                .align(Alignment.TopStart)
                .size(55.dp)
                .zIndex(1f)
                .clickable(onClick = onNavigateBack)
        ) {
            Icon(
                painter = painterResource(R.drawable.backarrow_icon),
                contentDescription = "Back",
                tint = Color(0xFFE91E63),
                modifier = Modifier.fillMaxSize()
            )
        }
        Box{
            Image(
                painter = painterResource(id = R.drawable.background_clouds_7),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopStart
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "credits",
                fontSize = 45.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = MajorMonoDisplay
            )
            Spacer(modifier = Modifier.height(40.dp))
                Column(Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF81D4FA),
                        shape = RoundedCornerShape(16.dp))
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        RoundedCornerShape(16.dp))
                    .padding(16.dp))
                {
                        Text(
                            text = "Developer: ",
                            fontSize = 18.sp,
                            color = Color.White,
                            fontFamily = ManropeBold
                        )
                        Text(
                            text = "Dolinskaia Anastasia",
                            fontSize = 24.sp,
                            color = Color(0xFFE91E63),
                            fontWeight = FontWeight.Bold,
                            fontFamily = ManropeBold
                        )


                        Text(
                            text = "For: ",
                            fontSize = 18.sp,
                            color = Color.White,
                            fontFamily = ManropeBold
                        )
                        Text(
                            text = "College of Computer Science and Programming",
                            fontSize = 24.sp,
                            color = Color(0xFFE91E63),
                            fontWeight = FontWeight.Bold,
                            fontFamily = ManropeBold
                        )

                        Text(
                            text = "Supervisor:",
                            fontSize = 18.sp,
                            color = Color.White,
                            fontFamily = ManropeBold
                        )
                        Text(
                            text = "Aksenova Tatyana Gennadievna",
                            fontSize = 24.sp,
                            color = Color(0xFFE91E63),
                            fontWeight = FontWeight.Bold,
                            fontFamily = ManropeBold
                        )


                        Text(
                            text = "Sources:",
                            fontSize = 18.sp,
                            color = Color.White,
                            fontFamily = ManropeBold
                        )
                        Text(
                            text = "",
                            fontSize = 24.sp,
                            color = Color(0xFFE91E63),
                            fontWeight = FontWeight.Bold,
                            fontFamily = ManropeBold
                        )

                }
                Spacer(modifier = Modifier.height(40.dp))



        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreditsScreenPreview() {
    BeNativeTheme {
        CreditsScreen(onNavigateBack = {})
    }
}