package com.example.benative.presentation.screen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
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
import com.example.benative.domain.usecase.CompleteLessonUseCase
import com.example.benative.domain.usecase.GetLessonUseCase
import com.example.benative.domain.usecase.GetTasksUseCase
import com.example.benative.R
import com.example.benative.data.AuthManager
import com.example.benative.domain.Lesson
import com.example.benative.domain.LessonCompletionRequest
import com.example.benative.domain.Task
import com.example.benative.domain.TaskResult
import com.example.benative.domain.TaskUiState
import com.github.barteksc.pdfviewer.PDFView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import androidx.core.graphics.createBitmap
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.withTimeout
import java.io.BufferedInputStream
import java.io.FileOutputStream

@Composable
fun VideoPlayer(uri: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            playWhenReady = false
            volume = 1f
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
            PlayerView(context).apply {
                player = exoPlayer
                useController = true
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun PdfViewerFromUrl(
    pdfUrl: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var renderer by remember { mutableStateOf<PdfRenderer?>(null) }
    var pfd by remember { mutableStateOf<ParcelFileDescriptor?>(null) }
    var pageCount by remember { mutableIntStateOf(0) }
    var currentPage by remember { mutableIntStateOf(0) }

    // Загружаем и инициализируем renderer единожды при изменении URL
    LaunchedEffect(pdfUrl) {
        isLoading = true
        error = null
        try {
            val (r, f, descriptor) = loadRendererFromUrl(context, pdfUrl)
            renderer = r
            pfd = descriptor
            pageCount = r.pageCount
        } catch (t: Throwable) {
            error = "Ошибка: ${t.message}"
        } finally {
            isLoading = false
        }
    }

    // Закрываем PdfRenderer и PFD только когда сам Composable уходит
    DisposableEffect(pdfUrl) {
        onDispose {
            renderer?.close()
            pfd?.close()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(Color.LightGray)
            .padding(8.dp)
    ) {
        when {
            isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            error != null -> Text(error!!, color = Color.Red, modifier = Modifier.align(Alignment.Center))
            renderer == null || pageCount == 0 -> Text("No pages", modifier = Modifier.align(Alignment.Center))
            else -> Column(Modifier.fillMaxSize()) {
                PdfPage(
                    pdfRenderer = renderer!!,
                    pageIndex = currentPage,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clipToBounds()
                )
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { if (currentPage > 0) currentPage-- }, enabled = currentPage > 0) { Text("<") }
                    Text("${currentPage + 1} / $pageCount", modifier = Modifier.align(Alignment.CenterVertically))
                    Button(onClick = { if (currentPage < pageCount - 1) currentPage++ }, enabled = currentPage < pageCount - 1) { Text(">") }
                }
            }
        }
    }
}

suspend fun loadRendererFromUrl(
    context: Context,
    pdfUrl: String
): Triple<PdfRenderer, File, ParcelFileDescriptor> = withContext(Dispatchers.IO) {
    withTimeout(15_000) {
        val connection = URL(pdfUrl).openConnection() as HttpURLConnection
        connection.connectTimeout = 10_000
        connection.readTimeout = 10_000
        connection.connect()
        connection.inputStream.use { input ->
            val temp = File.createTempFile("tmp", ".pdf", context.cacheDir)
            temp.outputStream().use { output ->
                input.copyTo(output)
            }
            val pfd = ParcelFileDescriptor.open(temp, ParcelFileDescriptor.MODE_READ_ONLY)
            val renderer = PdfRenderer(pfd)
            Triple(renderer, temp, pfd)
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun PdfPage(pdfRenderer: PdfRenderer, pageIndex: Int, modifier: Modifier = Modifier) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Проверяем, жив ли renderer
    val rendererAlive = remember(pdfRenderer) {
        try {
            pdfRenderer.pageCount
            true
        } catch (_: IllegalStateException) {
            false
        }
    }

    LaunchedEffect(pageIndex, rendererAlive) {
        if (!rendererAlive) return@LaunchedEffect

        try {
            withContext(Dispatchers.IO) {
                val page = pdfRenderer.openPage(pageIndex)
                val renderedBitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                page.render(renderedBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                page.close()
                bitmap = renderedBitmap
            }
        } catch (e: IllegalStateException) {
            // PdfRenderer уже закрыт — просто выходим
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    if (bitmap != null) {
        Image(bitmap = bitmap!!.asImageBitmap(), contentDescription = null, modifier = modifier)
    } else {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
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
                Log.e("LessonLoadError", "Task or lesson loading error", error)
            }
        }
    }

    if (lesson != null) {

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                // Кнопка для перепрохождения урока находится после всех заданий
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
                        Text("Restart")
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
                            val request =
                                LessonCompletionRequest(lessonId = lessonId, results = details)
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
                        Text("Finish")
                    }
                }
            },
            topBar = {
                // Заголовок с кнопкой "назад"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp, top = 30.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(55.dp)
                            .clickable(onClick = onNavigateBack)
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
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        ) { innerPadding ->
            // основной прокручиваемый контент, с учётом отступа снизу
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)         // сюда Scaffold подставит bottomBar height
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Текст урока
                lesson!!.articleText?.let {
                    Text(
                        text = it, // Здесь можно добавить текст урока
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp),
                        fontSize = 20.sp,
                        color = Color.Black
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
                            PdfViewerFromUrl(media)
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
                modifier = Modifier.padding(bottom = 8.dp).weight(1f),
                color = Color.Black
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
            Text("Check")
        }
        if (state.isChecked.value) {
            Text(
                text = if (state.isCorrect.value) "Correct!" else "Wrong :(",
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
                modifier = Modifier.padding(bottom = 8.dp).weight(1f),
                color = Color.Black
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
                text = if (state.isCorrect.value) "Correct!" else "Wrong :(",
                color = if (state.isCorrect.value) Color.Green else Color.Red,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}






