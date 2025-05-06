package com.example.benative.ui.screen

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.benative.Api.ApiModule
import com.example.benative.Api.UseCase.GetUserUseCase
import com.example.benative.Api.UseCase.UploadAvatarUseCase
import com.example.benative.R
import com.example.benative.server.AuthManager
import com.example.benative.ui.theme.ManropeBold
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File

@OptIn(UnstableApi::class)
fun getFileFromUri(context: Context, uri: Uri): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val fileName = uri.lastPathSegment ?: "avatar.jpg"
        val file = File(context.cacheDir, fileName)
        file.outputStream().use { output ->
            inputStream.copyTo(output)
        }
        inputStream.close()
        file
    } catch (e: Exception) {
        Log.e("FileError", "Ошибка извлечения файла из Uri", e)
        null
    }
}
@OptIn(UnstableApi::class)
@Composable
fun ProfileScreen(onNavigateBack: () -> Unit, onLogoutClick: () -> Unit) {
    var avatarUrl by remember { mutableStateOf<String?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isUploading by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            coroutineScope.launch {
                isUploading = true

                val file = getFileFromUri(context, uri)
                val token = AuthManager.getToken(context).first()
                if (file != null && token != null) {
                    try {
                        Log.d("AvatarUpload", "Uploading file: ${file.name}")
                        UploadAvatarUseCase(token, file)
                        avatarUrl = null // чтобы AsyncImage пересоздался
                        avatarUrl = GetUserUseCase(token).avatar // получаем новую ссылку
                        Log.d("AvatarUpload", "New avatar URL: $avatarUrl")
                    } catch (e: Exception) {
                        errorMessage = "Ошибка загрузки аватара"
                    }finally {
                        isUploading = false
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val token = AuthManager.getToken(context).first()

                if (token == null) {
                    errorMessage = "Not authenticated"
                    isLoading = false
                    return@launch
                }

                val response = GetUserUseCase(token)
                avatarUrl = response.avatar

            } catch (e: Exception) {
                errorMessage = "Ошибка получения данных"
            }
        }
    }

    Box(modifier = Modifier.background(Color(0xFFB0E0E6)).fillMaxSize()) {


        Image(
            painter = painterResource(id = R.drawable.background_clouds_5),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopStart
        )

        Box(
            modifier = Modifier
                .padding(16.dp, 30.dp, 16.dp, 16.dp)
                .align(Alignment.TopStart)
                .size(55.dp)
                .clickable(onClick = onNavigateBack)
        ) {
            Icon(
                painter = painterResource(R.drawable.backarrow_icon),
                contentDescription = "Back",
                tint = Color(0xFFE91E63),
                modifier = Modifier.fillMaxSize()
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp, 30.dp, 16.dp, 16.dp)
                .size(55.dp)
                .clickable(onClick = onLogoutClick)
        ) {
            Icon(
                painter = painterResource(R.drawable.logout_icon),
                contentDescription = "Logout",
                tint = Color(0xFFE91E63),
                modifier = Modifier.fillMaxSize()
            )
        }



        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .wrapContentSize()
        ) {

            // Показываем выбранное изображение, если есть, иначе — из сети
            if (selectedImageUri != null) {
                AsyncImage(
                    ImageRequest.Builder(LocalContext.current)
                        .data(selectedImageUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Selected Avatar",
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentScale = ContentScale.Crop
                )
            } else {
                AsyncImage(
                    ImageRequest.Builder(LocalContext.current)
                        .data(avatarUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(Color(0x508BC34A))
                        .border(3.dp, Color(0xFF8BC34A), CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(contentAlignment = Alignment.Center){
                // Нижний текст (обводка)
                Text(
                    text = "Username",
                    fontSize = 32.5.sp,
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
                    text = "Username",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = ManropeBold
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { imagePickerLauncher.launch("image/*") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFf5e2e9)),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color(0xFFE91E63)
                )
            ) {
                Text(
                    "Change avatar",
                    color = Color(0xFFE91E63),
                    fontFamily = ManropeBold,
                    fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Handle change name */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFf5e2e9)),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color(0xFFE91E63)
                )
            ) {
                Text("Change name",
                    color = Color(0xFFE91E63),
                    fontFamily = ManropeBold,
                    fontSize = 20.sp
                )
            }


        }
    }
    if (isUploading ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x88000000))
                .clickable(enabled = false) { /* блокирует всё ниже */ }
                .zIndex(1f),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    }
}



@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(onNavigateBack = {}, onLogoutClick = {})
}