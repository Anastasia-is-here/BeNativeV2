package com.example.benative.ui.screen

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
import androidx.compose.material3.IconButton
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
import com.example.benative.Api.UseCase.GetUserStatsUseCase
import com.example.benative.R
import com.example.benative.navigation.Screen
import com.example.benative.server.AuthManager
import com.example.benative.server.UserStatsResponse
import com.example.benative.ui.theme.BeNativeTheme
import com.example.benative.ui.theme.MajorMonoDisplay
import com.example.benative.ui.theme.ManropeBold
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun StatsScreen(onNavigateBack: () -> Unit) {
    var stats by remember { mutableStateOf<UserStatsResponse?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(true) {
        val token = AuthManager.getToken(context).first()
        if (token != null) {
            coroutineScope.launch {
                stats = try {
                    GetUserStatsUseCase(token)
                } catch (e: Exception) {
                    null
                }
            }
        }
    }

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
                text = "your   ",
                fontSize = 45.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = MajorMonoDisplay
            )
            Text(
                text = "   stats",
                fontSize = 45.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = MajorMonoDisplay
            )
            Spacer(modifier = Modifier.height(80.dp))
            if (stats != null) {
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
                    Row {
                        Text(
                            text = "lessons completed: ",
                            fontSize = 24.sp,
                            color = Color.White,
                            fontFamily = ManropeBold
                        )
                        Text(
                            text = "${stats!!.completedLessons}",
                            fontSize = 24.sp,
                            color = Color(0xFFE91E63),
                            fontWeight = FontWeight.Bold,
                            fontFamily = ManropeBold
                        )
                    }

                    Row{
                        Text(
                            text = "average % per lesson: ",
                            fontSize = 24.sp,
                            color = Color.White,
                            fontFamily = ManropeBold
                        )
                        Text(
                            text = "${stats!!.averageProgress}%",
                            fontSize = 24.sp,
                            color = Color(0xFFE91E63),
                            fontWeight = FontWeight.Bold,
                            fontFamily = ManropeBold
                        )
                    }

                    Row{
                        Text(
                            text = "tasks completed: ",
                            fontSize = 24.sp,
                            color = Color.White,
                            fontFamily = ManropeBold
                        )
                        Text(
                            text = "${stats!!.completedTasks}",
                            fontSize = 24.sp,
                            color = Color(0xFFE91E63),
                            fontWeight = FontWeight.Bold,
                            fontFamily = ManropeBold
                        )
                    }

                    Row{
                        Text(
                            text = "total experience: ",
                            fontSize = 24.sp,
                            color = Color.White,
                            fontFamily = ManropeBold
                        )
                        Text(
                            text = "${stats!!.totalExperience}",
                            fontSize = 24.sp,
                            color = Color(0xFFE91E63),
                            fontWeight = FontWeight.Bold,
                            fontFamily = ManropeBold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))


            } else {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatsScreenPreview() {
    BeNativeTheme {
        StatsScreen(onNavigateBack = {})
    }
}