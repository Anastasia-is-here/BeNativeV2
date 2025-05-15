package com.example.benative.presentation.screen

import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.benative.domain.usecase.GetUserUseCase
import com.example.benative.R
import com.example.benative.presentation.navigation.Screen
import com.example.benative.data.AuthManager
import com.example.benative.presentation.theme.BeNativeTheme
import com.example.benative.presentation.theme.ManropeBold
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

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC))
    ) {
        val screenWidth = LocalContext.current.resources.displayMetrics.widthPixels / LocalContext.current.resources.displayMetrics.density
        val screenHeight = maxHeight
        val circleSize = screenWidth * 0.35f
        val buttonSize = (screenWidth * 0.35f).dp
        val iconSize = (screenWidth * 0.1f).dp
        val textSize = screenWidth.coerceAtMost(360f) / 12
        val progressSize = (screenWidth * 0.55).dp

        // фон и иконка
        Image(
            painter = painterResource(id = R.drawable.background_clouds_6),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopStart
        )

        Box(
            modifier = Modifier
                .padding(start = 16.dp, top = 30.dp)
                .align(Alignment.TopStart)
                .size(55.dp)
                .clickable { onNavigateTo(Screen.ProfileScreen) }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_profile),
                contentDescription = "Profile",
                tint = Color(0xFFE91E63),
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Level
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "Level $level",
                    fontSize = (textSize.sp.value + 0.5f).sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8BC34A),
                    style = TextStyle(drawStyle = Stroke(width = 8f)),
                    fontFamily = ManropeBold
                )
                Text(
                    text = "Level $level",
                    fontSize = textSize.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = ManropeBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Прогресс
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { progress.toFloat() / 100 },
                    modifier = Modifier.size(progressSize),
                    color = Color(0xFF8BC34A),
                    strokeWidth = (0.102f * progressSize.value).dp,
                    trackColor = Color(0xFFF8BBD0),
                    strokeCap = StrokeCap.Round
                )
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "$progress%",
                        fontSize = (textSize.sp.value + 0.5f).sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8BC34A),
                        style = TextStyle(drawStyle = Stroke(width = 8f)),
                        fontFamily = ManropeBold
                    )
                    Text(
                        text = "$progress%",
                        fontSize = textSize.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = ManropeBold
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Кнопки
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircleButton("Lessons", R.drawable.ic_lessons, buttonSize, iconSize, textSize.sp) {
                        onNavigateTo(Screen.LessonScreen)
                    }
                    CircleButton("Statistics", R.drawable.ic_statistics, buttonSize, iconSize, textSize.sp) {
                        onNavigateTo(Screen.StatsScreen)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                CircleButton("Credits", R.drawable.ic_credits, buttonSize, iconSize, textSize.sp) {
                    onNavigateTo(Screen.CreditsScreen)
                }
            }
        }
    }
}
@Composable
fun CircleButton(
    text: String,
    iconId: Int,
    size: Dp,
    iconSize: Dp,
    fontSize: TextUnit,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
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
                modifier = Modifier.size(iconSize)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = text,
                fontSize = fontSize * 0.8,
                color = Color(0xFFE91E63),
                fontWeight = FontWeight.Bold,
                fontFamily = ManropeBold
            )
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
fun MainScreenPreview() {
    BeNativeTheme {
        MainScreen()
    }
}