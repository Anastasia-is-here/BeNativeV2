package com.example.benative.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.benative.R
import com.example.benative.navigation.Screen
import com.example.benative.Api.ApiClient
import com.example.benative.server.AuthManager
import com.example.benative.server.Lesson
import com.example.benative.ui.theme.BeNativeTheme
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import kotlinx.coroutines.launch

object LessonRepository {
    suspend fun fetchLessons(token: String): List<Lesson> {
        return ApiClient.client.get(ApiClient.lessonsUrl) {
            headers {
                append("Authorization", "Bearer $token")
            }
        }.body()
    }
}

@Composable
fun LessonScreen(onNavigateTo: (Screen) -> Unit = {}) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var lessons by remember { mutableStateOf<List<Lesson>>(emptyList()) }

    LaunchedEffect(Unit) {
        val token = AuthManager.getToken(context)
        if (token != null) {
            coroutineScope.launch {
                runCatching {
                    LessonRepository.fetchLessons(token)
                }.onSuccess {
                    lessons = it
                }.onFailure {
                    // обработка ошибки (логгирование, сообщение)
                }
            }
        }
    }

    Box(
        modifier = Modifier.background(Color(0xFFB0E0E6)).fillMaxSize()
    ) {
        IconButton(
            onClick = { onNavigateTo(Screen.MainScreen) }, // Перенаправление на MainScreen
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
                .zIndex(1f)
                .size(60.dp)// Размещение в левом верхнем углу
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
        Column(
            modifier = Modifier
                .padding(16.dp),
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
                    .size(120.dp)
                    .padding(end = 16.dp)
            )
            Text(
                text = lesson.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFCC6699),
                fontSize = 22.sp
            )
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