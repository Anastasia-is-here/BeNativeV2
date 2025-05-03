package com.example.benative.ui.screen

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerControlView
import androidx.media3.ui.PlayerView
import com.example.benative.R
import com.example.benative.server.AuthManager
import com.example.benative.server.Lesson
import com.example.benative.server.LessonRepository
import com.example.benative.server.TaskResponse
import kotlinx.coroutines.launch

@Composable
fun VideoPlayer(uri: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            playWhenReady = false
        }
    }

    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    AndroidView(
        factory = {
            PlayerView(context).apply {
                player = exoPlayer
                useController = true
            }
        },
        modifier = modifier
    )
}

@OptIn(UnstableApi::class)
@Composable
fun AudioPlayer(uri: String) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            playWhenReady = false
        }
    }

    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    AndroidView(
        factory = {
            PlayerControlView(context).apply {
                player = exoPlayer
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(UnstableApi::class)
@Composable
fun TaskScreen(
    lessonId: Int, // Добавляем параметр для передачи lessonId
    onNavigateBack: () -> Unit,
    onTaskChecked: (TaskResponse, Boolean) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var lesson by remember { mutableStateOf<Lesson?>(null) }
    var tasks by remember { mutableStateOf<List<TaskResponse>>(emptyList()) }

    Log.d("Debug", "Loading lesson with ID: $lessonId")

    LaunchedEffect(lessonId) {
        val token = AuthManager.getToken(context)
        if (token != null) {
            coroutineScope.launch {
                runCatching {
                    // Загружаем данные урока и задания
                    val lessonResponse = com.example.benative.server.LessonRepository.fetchLessonById(token, lessonId)
                    val tasksResponse = LessonRepository.fetchTasksForLesson(token, lessonId)

                    lesson = lessonResponse
                    tasks = tasksResponse

                }.onFailure { error ->
                    Log.e("LessonLoadError", "Ошибка загрузки урока или заданий", error)
                }
            }
        }
    }

    if (lesson != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Заголовок с кнопкой "назад"
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.backarrow_icon),
                        contentDescription = "Back",
                        tint = Color(0xFFE91E63)
                    )
                }

                Text(
                    text = lesson!!.title,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            // Текст урока
            Text(
                text = lesson!!.title, // Здесь можно добавить текст урока
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )


            // Вставка видео или аудио (если есть)
            if (!lesson!!.mediaUrl.isNullOrEmpty()) {
                lesson!!.mediaUrl?.let {
                    Log.d("VideoDebug", "Playing video from URL: ${lesson!!.mediaUrl}")
                    VideoPlayer(uri = it, modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .padding(vertical = 8.dp))
                }
            }

//            if (!lesson.audioUrl.isNullOrEmpty()) {
//                AudioPlayer(uri = lesson.audioUrl)
//            }

            // Задания
//            tasks.forEach { task ->
//                TaskItem(task = task, onTaskChecked = onTaskChecked)
//            }
        }
    } else {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ){
            // Пока урок загружается, можно показать прогресс
            CircularProgressIndicator()
        }
    }
}



