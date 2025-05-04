package com.example.benative.ui.screen

import androidx.annotation.OptIn
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.benative.Api.UseCase.GetUserUseCase
import com.example.benative.R
import com.example.benative.navigation.Screen
import com.example.benative.server.AuthManager
import com.example.benative.ui.theme.BeNativeTheme
import com.example.benative.ui.theme.ManropeBold
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
@Composable
fun MainScreen(onNavigateTo: (Screen) -> Unit = {}) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Состояние для прогресса
    var progress by remember { mutableIntStateOf(0) } // Начальное значение
    var level by remember { mutableIntStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val token = AuthManager.getToken(context).first()
                if (token == null) {
                    errorMessage = "Not authenticated"
                    isLoading = false
                    return@launch
                }
                try {
                    val userResponse = GetUserUseCase(token)
                    // Преобразуем experience в процент (предположим, максимум для уровня 1 = 100)
                    val maxExperienceForLevel = 100
                    progress = (userResponse.experience % maxExperienceForLevel)
                    Log.d("EXP", progress.toString() + " " + userResponse.experience.toString())
                    level = (userResponse.experience / maxExperienceForLevel)
                } catch (e: Exception){
                    errorMessage = e.message
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFB2EBF2), Color(0xFFE1F5FE)) // Похоже на твой фон
                )
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_clouds_6),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopStart
        )
        // Иконка профиля
        IconButton(
            onClick = { /* Переход к профилю */ },
            modifier = Modifier.align(Alignment.TopStart)
                .size(60.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_profile),
                contentDescription = "Profile",
                tint = Color(0xFFE91E63),
                modifier = Modifier.fillMaxSize()
            )
        }

        // Контент по центру
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp, bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Box {
                // Нижний текст (обводка)
                Text(
                    text = "Level ${level.toInt()}",
                    fontSize = 33.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8BC34A), // Зеленый цвет обводки
                    style = TextStyle(
                        drawStyle = Stroke(
                            width = 8f // Толщина обводки
                        )
                    ),
                    fontFamily = ManropeBold
                )
                // Верхний текст (основной белый)
                Text(
                    text = "Level ${level.toInt()}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = ManropeBold
                )
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Круговой прогресс
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { progress.toFloat() / 100 },
                    modifier = Modifier.size(200.dp),
                    color = Color(0xFF8BC34A), // Зеленый цвет
                    strokeWidth = 17.dp,
                        trackColor = Color(0xFFF8BBD0), // Розовый бэкграунд круга
                    strokeCap = StrokeCap.Round,
                )
                Box {
                    // Нижний текст (обводка)
                    Text(
                        text = "${(progress.toInt())}%",
                        fontSize = 33.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8BC34A), // Зеленый цвет обводки
                        style = TextStyle(
                            drawStyle = Stroke(
                                width = 8f // Толщина обводки
                            )
                        ),
                        fontFamily = ManropeBold
                    )
                    // Верхний текст (основной белый)
                    Text(
                        text = "${(progress.toInt())}%",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = ManropeBold
                    )
                }
            }

            Spacer(modifier = Modifier.size(75.dp))

            // Кнопки внизу
            Column(
                modifier = Modifier
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircleButton(text = "Lessons", iconId = R.drawable.ic_lessons, onClick = { onNavigateTo(Screen.LessonScreen) })
                    Spacer(modifier = Modifier.width(30.dp))
                    CircleButton(
                        text = "Statistics",
                        iconId = R.drawable.ic_statistics
                    ) { /* переход */ }
                }
                Spacer(modifier = Modifier.height(16.dp))
                CircleButton(text = "Credits", iconId = R.drawable.ic_credits) { /* переход */ }
            }
        }
    }
}

@Composable
fun CircleButton(
    text: String,
    iconId: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(140.dp)
            .clip(CircleShape)
            .background(Color(0x99f5e2e9))
            .clickable { onClick() }
            .border(width = 2.dp, color = Color(0xFFD97BA4), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = text,
                tint = Color(0xFFE91E63),
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = text,
                fontSize = 20.sp,
                color = Color(0xFFE91E63),
                fontWeight = FontWeight.Bold,
                fontFamily = ManropeBold
            )
        }

    }

}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    BeNativeTheme {
        MainScreen()
    }
}