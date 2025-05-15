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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.benative.domain.usecase.GetLessonsUseCase
import com.example.benative.R
import com.example.benative.presentation.navigation.Screen
import com.example.benative.data.AuthManager
import com.example.benative.domain.Lesson
import com.example.benative.presentation.theme.BeNativeTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
@Composable
fun LessonScreen(onNavigateTo: (Screen) -> Unit = {}) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var lessons by remember { mutableStateOf<List<Lesson>>(emptyList()) }

    LaunchedEffect(Unit) {
        val token = AuthManager.getToken(context).first()
        if (token != null) {
            coroutineScope.launch {
                runCatching {
                    GetLessonsUseCase(token)

                }.onSuccess {
                    lessons = it
                }.onFailure { error ->
                    Log.e("LessonLoadError", "Ошибка загрузки урока или заданий", error)
                }
                Log.d("Lessons", GetLessonsUseCase(token).toString())
            }
        }
    }

    Box(
        modifier = Modifier.background(Color(0xFFB3E5FC)).fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp, 30.dp, 16.dp, 16.dp)
                .align(Alignment.TopStart)
                .size(55.dp)
                .zIndex(1f)
                .clickable(onClick = { onNavigateTo(Screen.MainScreen) })
        ) {
            Icon(
                painter = painterResource(R.drawable.backarrow_icon),
                contentDescription = "Back",
                tint = Color(0xFFE91E63),
                modifier = Modifier.fillMaxSize()
            )
        }

        Image(
            painter = painterResource(id = R.drawable.background_clouds_3),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
                .zIndex(0f),
            contentScale = ContentScale.Crop
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                        .padding(top = 80.dp)
        ) {
            items(lessons) { lesson ->
                LessonCard(lesson = lesson){
                    onNavigateTo(Screen.TasksScreen(lesson.id))
                }
            }
        }

    }
}

@Composable
fun LessonCard(lesson: Lesson, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color.White, RoundedCornerShape(16.dp))
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        color = Color(0xFFD6F2FF)
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val maxWidth = maxWidth
            val fontSize = when {
                maxWidth < 140.dp -> 16.sp
                maxWidth < 180.dp -> 18.sp
                maxWidth < 220.dp -> 20.sp
                else -> 22.sp
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(lesson.iconUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = lesson.title,
                    modifier = Modifier
                        .size(maxWidth * 0.6f)
                        .padding(bottom = 8.dp)
                )
                Text(
                    text = lesson.title,
                    fontSize = fontSize,
                    color = Color(0xFFCC6699),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LessonScreenPreview() {
    BeNativeTheme {
        LessonScreen()
    }
}