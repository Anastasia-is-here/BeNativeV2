package com.example.benative.ui.screen

import com.example.benative.server.ApiClient
import com.example.benative.server.Lesson
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.benative.navigation.Screen
import com.example.benative.server.AuthManager
import com.example.benative.ui.theme.BeNativeTheme
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

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(lessons) { lesson ->
            LessonCard(lesson = lesson)
        }
    }
}

@Composable
fun LessonCard(lesson: Lesson) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFD6F2FF).copy(alpha = 0.15f)),
        tonalElevation = 4.dp,
        shadowElevation = 8.dp,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(lesson.iconUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = lesson.title,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp)
            )
            Text(
                text = lesson.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFCC6699)
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