package com.example.benative.ui.screen

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerControlView
import androidx.media3.ui.PlayerView
import com.example.benative.Api.UseCase.CompleteLessonUseCase
import com.example.benative.Api.UseCase.GetLessonUseCase
import com.example.benative.Api.UseCase.GetTasksUseCase
import com.example.benative.R
import com.example.benative.server.AuthManager
import com.example.benative.server.Lesson
import com.example.benative.server.LessonCompletionRequest
import com.example.benative.server.Task
import com.example.benative.server.TaskResult
import com.example.benative.server.TaskUiState
import com.example.benative.ui.theme.ManropeBold
import io.ktor.client.call.body
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

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

@Composable
fun PdfViewer(pdfUrl: String) {
    AndroidView(
        factory = { context ->
            android.webkit.WebView(context).apply {
                settings.javaScriptEnabled = true
                // Google Docs Viewer
                loadUrl("https://docs.google.com/gview?embedded=true&url=$pdfUrl")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    )
}

@OptIn(UnstableApi::class)
@Composable
fun TaskScreen(
    lessonId: Int, // Добавляем параметр для передачи lessonId
    onNavigateBack: () -> Unit,
    onTaskChecked: (Task, Boolean) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var lesson by remember { mutableStateOf<Lesson?>(null) }
    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    val taskStates = remember { mutableStateMapOf<Int, TaskUiState>() }
    var forceReset by remember { mutableStateOf(false) }

    LaunchedEffect(lessonId) {
        val token = AuthManager.getToken(context).first()
        if (token != null) {
            runCatching {
                lesson = GetLessonUseCase(token, lessonId)
                tasks = GetTasksUseCase(token, lessonId)

                tasks.forEach { task ->
                    taskStates[task.id] = TaskUiState()
                }

                forceReset = false
            }.onFailure { error ->
                Log.e("LessonLoadError", "Ошибка загрузки урока или заданий", error)
            }
        }
    }

    if (lesson != null) {
        Box(
            modifier = Modifier.background(Color(0xFFB0E0E6)).fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Заголовок с кнопкой "назад"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.size(55.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.backarrow_icon),
                            contentDescription = "Back",
                            tint = Color(0xFFE91E63),
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Text(
                        text = lesson!!.title,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(start = 22.dp),
                        fontWeight = FontWeight.Bold
                    )
                }

                // Текст урока
                lesson!!.articleText?.let {
                    Text(
                        text = it, // Здесь можно добавить текст урока
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp),
                        fontSize = 20.sp
                    )
                }

                lesson!!.mediaUrl?.let { media ->
                    when {
                        media.endsWith(".mp4", ignoreCase = true) -> {
                            VideoPlayer(
                                uri = media,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .padding(vertical = 8.dp)
                            )
                        }
                        media.endsWith(".mp3", ignoreCase = true) -> {
                            AudioPlayer(media)
                        }
                        media.endsWith(".pdf", ignoreCase = true) -> {
                            PdfViewer(media)
                        }
                    }
                }

                // Задания
                tasks.forEach { task ->
                    val state = taskStates[task.id]!!

                    when (task.taskType) {
                        "TextInput" -> {
                            TextInputTaskItem(
                                task = task,
                                state = state,
                                onCheck = {
                                    val correct = state.userInput.value.trim()
                                        .equals(task.correctAnswer.trim(), ignoreCase = true)
                                    state.isChecked.value = true
                                    state.isCorrect.value = correct
                                    onTaskChecked(task, correct)
                                }
                            )
                        }
                        "SingleChoice" -> {
                            SingleChoiceTaskItem(
                                task = task,
                                state = state,
                                onAnswerSelected = { selectedOption ->
                                    val correct = selectedOption.trim()
                                        .equals(task.correctAnswer.trim(), ignoreCase = true)
                                    state.isCorrect.value = correct
                                    onTaskChecked(task, correct)
                                },
                                onRetry = {
                                    // Здесь вы можете сбросить состояние задания
                                    taskStates[task.id] = TaskUiState() // Сбросить состояние задания
                                }
                            )
                        }
                    }
                }
            }

            // Кнопка для перепрохождения урока находится после всех заданий
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {
                // Перепройти урок
                Button(
                    onClick = {
                        // сброс состояния
                        taskStates.values.forEach { s ->
                            s.userInput.value = ""
                            s.isChecked.value = false
                            s.isCorrect.value = false
                        }
                    },
                    enabled = taskStates.values.any { it.isChecked.value } // хотя бы одно проверено
                ) {
                    Text("Перепройти урок")
                }

                // Завершить урок
                val canFinish = taskStates.values.any { it.isChecked.value }
                Button(
                    onClick = {
                        // собираем детали
                        val details: List<TaskResult> = taskStates.map { (taskId, s) ->
                            TaskResult(
                                taskId = taskId,
                                isCompleted = s.isCorrect.value,
                                earnedExp = if (s.isCorrect.value)
                                    tasks.first { it.id == taskId }.experienceReward
                                else 0
                            )
                        }
                        val request = LessonCompletionRequest(lessonId = lessonId, results = details)
                        // вызываем useCase для отправки на сервер

                        coroutineScope.launch {
                            val token = AuthManager.getToken(context).first()
                            if (token != null) {
                            CompleteLessonUseCase(token, request)
                            onNavigateBack() // или перейти на MainScreen
                                }
                        }
                    },
                    enabled = canFinish
                ) {
                    Text("Завершить урок")
                }
            }
        }
    } else {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Пока урок загружается, можно показать прогресс
            CircularProgressIndicator()
        }
    }
}
@Composable
fun TextInputTaskItem(
    task: Task,
    state: TaskUiState,
    onCheck: () -> Unit
) {

    Column(modifier = Modifier.padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = task.taskText,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp).weight(1f)
            )
            Text(
                text = "${if (state.isChecked.value && state.isCorrect.value) task.experienceReward else 0}/${task.experienceReward}",
                fontSize = 16.sp,
                color = Color(0xFFE91E63)
            )
        }
        OutlinedTextField(
            value = state.userInput.value,
            onValueChange = { state.userInput.value = it },
            enabled = !state.isChecked.value,
            /* … */
        )
        Button(
            onClick = onCheck,
            enabled = !state.isChecked.value
        ) {
            Text("Проверить")
        }
        if (state.isChecked.value) {
            Text(
                text = if (state.isCorrect.value) "Верно!" else "Неверно",
                color = if (state.isCorrect.value) Color.Green else Color.Red
            )
        }
    }
}

@Composable
fun SingleChoiceTaskItem(
    task: Task,
    state: TaskUiState,
    onAnswerSelected: (String) -> Unit,
    onRetry: () -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ){
            Text(
                text = task.taskText,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp).weight(1f)
            )
            Text(
                text = "${if (state.isChecked.value && state.isCorrect.value) task.experienceReward else 0}/${task.experienceReward}",
                fontSize = 16.sp,
                color = Color(0xFFE91E63),
                fontWeight = FontWeight.SemiBold
            )
        }


        val options = remember(task.options) {
            try {
                Json.decodeFromString<List<String>>(task.options ?: "[]")
            } catch (e: Exception) {
                emptyList()
            }
        }

        options.forEach { option ->
            val isSelected = state.userInput.value == option
            val isCorrectAnswer = option.equals(task.correctAnswer, ignoreCase = true)

            val backgroundColor = when {
                !state.isChecked.value -> Color.LightGray
                isSelected && isCorrectAnswer -> Color(0xFF4CAF50) // зелёный
                isSelected && !isCorrectAnswer -> Color(0xFFF44336) // красный
                !isSelected && isCorrectAnswer -> Color(0xFF81C784) // светло-зелёный
                else -> Color.LightGray
            }

            val enabled = !state.isChecked.value

            Button(
                onClick = {
                    if (enabled) {
                        state.userInput.value = option
                        state.isChecked.value = true
                        state.isCorrect.value = isCorrectAnswer
                        onAnswerSelected(option)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
                enabled = enabled || isSelected, // Оставляем выбранную активной для цвета
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(text = option)
            }
        }

        if (state.isChecked.value) {
            Text(
                text = if (state.isCorrect.value) "Верно!" else "Неверно",
                color = if (state.isCorrect.value) Color.Green else Color.Red,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}






