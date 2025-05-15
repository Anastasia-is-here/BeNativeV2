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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
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
    val screenWidth = LocalContext.current.resources.displayMetrics.widthPixels / LocalContext.current.resources.displayMetrics.density

    val titleFontSize = if (screenWidth < 360) 30.sp else 45.sp
    val labelFontSize = if (screenWidth < 360) 14.sp else 18.sp
    val valueFontSize = if (screenWidth < 360) 18.sp else 24.sp
    val iconSize = if (screenWidth < 360) 40.dp else 55.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC))
    ) {
        // Назад
        Box(
            modifier = Modifier
                .padding(16.dp, 30.dp, 16.dp, 16.dp)
                .align(Alignment.TopStart)
                .size(iconSize)
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

        // Облака
        Image(
            painter = painterResource(id = R.drawable.background_clouds_7),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopStart
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "credits",
                fontSize = titleFontSize,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = MajorMonoDisplay
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF81D4FA),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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
                    text = "Kiyuk Maria Valerievna",
                    fontSize = 24.sp,
                    color = Color(0xFFE91E63),
                    fontWeight = FontWeight.Bold,
                    fontFamily = ManropeBold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
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
fun CreditsScreenPreview() {
    BeNativeTheme {
        CreditsScreen(onNavigateBack = {})
    }
}