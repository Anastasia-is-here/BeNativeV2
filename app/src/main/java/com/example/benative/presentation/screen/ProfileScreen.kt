package com.example.benative.presentation.screen

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.benative.R
import com.example.benative.data.AuthManager
import com.example.benative.domain.usecase.DeleteAvatarUseCase
import com.example.benative.domain.usecase.GetUserUseCase
import com.example.benative.domain.usecase.UpdateUserNameUseCase
import com.example.benative.domain.usecase.UploadAvatarUseCase
import com.example.benative.presentation.component.button.ButtonSecondary
import com.example.benative.presentation.navigation.Screen
import com.example.benative.presentation.theme.ManropeBold
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
fun ProfileScreen(onNavigateBack: () -> Unit, onNavigateTo: (Screen) -> Unit) {
    var avatarUrl by remember { mutableStateOf<String?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isUploading by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var username by remember { mutableStateOf("Username") } 
    var newName by remember { mutableStateOf("") }
    var isEditingName by remember { mutableStateOf(false) }

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            coroutineScope.launch {
                isUploading = true

//                val file = getFileFromUri(context, uri)
//                val token = AuthManager.getToken(context).first()
//                if (file != null && token != null) {
//                    try {
//                        Log.d("AvatarUpload", "Uploading file: ${file.name}")
//                        UploadAvatarUseCase(token, file)
//                        avatarUrl = null
//                        avatarUrl = GetUserUseCase(token).avatar
//                        Log.d("AvatarUpload", "New avatar URL: $avatarUrl")
//                    } catch (e: Exception) {
//                        errorMessage = "Avatar loading error"
//                    }finally {
//                        isUploading = false
//                    }
//                }
            }
        }
    }

//    LaunchedEffect(Unit) {
//        coroutineScope.launch {
//            try {
//                val token = AuthManager.getToken(context).first()
//
//                if (token == null) {
//                    errorMessage = "Not authenticated"
//                    isLoading = false
//                    return@launch
//                }
//
//                val response = GetUserUseCase(token)
//                avatarUrl = response.avatar
//
//                username = response.name
//                newName = username
//
//            } catch (e: Exception) {
//                errorMessage = "Error"
//            }
//        }
//    }

    Box(modifier = Modifier.background(Color(0xFFB3E5FC)).fillMaxSize()) {


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
                .clickable{ showLogoutDialog = true }
        ) {
            Icon(
                painter = painterResource(R.drawable.logout_icon),
                contentDescription = "Logout",
                tint = Color(0xFFE91E63),
                modifier = Modifier.fillMaxSize()
            )
        }

        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Confirmation") },
                text = { Text("Are you sure you want to log out?") },
                confirmButton = {
                    TextButton(onClick = {
                        showLogoutDialog = false
                        coroutineScope.launch {
                            //AuthManager.clearToken(context)
                            onNavigateTo(Screen.SignInScreen)
                        }
                    }) {
                        Text("Log out")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

//        if (showDeleteDialog) {
//            AlertDialog(
//                onDismissRequest = { showDeleteDialog = false },
//                title = { Text("Delete avatar") },
//                text = { Text("Are you sure you want to delete your avatar?") },
//                confirmButton = {
//                    TextButton(onClick = {
//                        showDeleteDialog = false
//                        coroutineScope.launch {
//                            val token = AuthManager.getToken(context).first()
//                            if (token != null) {
//                                DeleteAvatarUseCase(token)
//                                avatarUrl = null
//                                avatarUrl = GetUserUseCase(token).avatar
//                            }
//                        }
//                    }) { Text("Delete") }
//                },
//                dismissButton = {
//                    TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
//                }
//            )
//        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .wrapContentSize()
        ) {

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

            if (isEditingName) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 32.dp)
                ) {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("New name") },
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 24.sp),
                        modifier = Modifier
                            .weight(1f)
                    )
                    IconButton(
                        onClick = {
                            isEditingName = false
                            newName = username
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_cancel), 
                            contentDescription = "Cancel",
                            tint = Color(0xFFE91E63)
                        )
                    }
                }
            }else {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = username,
                        fontSize = 32.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8BC34A),
                        style = TextStyle(drawStyle = Stroke(width = 8f)),
                        fontFamily = ManropeBold
                    )
                    Text(
                        text = username,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = ManropeBold
                    )
                }
            }



            Spacer(modifier = Modifier.height(32.dp))

                ButtonSecondary(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier.width(200.dp)
                ) {
                    Text(
                        "Change avatar",
                        color = Color(0xFFE91E63),
                        fontFamily = ManropeBold,
                        fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

            ButtonSecondary(
                    onClick = {showDeleteDialog = true},
                    modifier = Modifier.width(200.dp)
                ) {
                    Text(
                        "Delete avatar",
                        color = Color(0xFFE91E63),
                        fontFamily = ManropeBold,
                        fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

            ButtonSecondary(
                    onClick = {
//                        if (isEditingName) {
//                            coroutineScope.launch {
//                                try {
//                                    val token = AuthManager.getToken(context).first()
//                                    if (token != null) {
//                                        UpdateUserNameUseCase(token, newName)
//                                    }
//                                    username = newName
//                                    isEditingName = false
//                                } catch (e: Exception) {
//                                    errorMessage = "Failed to upload new name"
//                                }
//                            }
//                        } else {
//                            isEditingName = true
//                        }
                    },
                    modifier = Modifier.width(200.dp)
                ) {
                    Text(
                        if (isEditingName) "Save" else "Change name",
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
                .clickable(enabled = false) { }
                .zIndex(1f),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
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
fun ProfileScreenPreview() {
    ProfileScreen(onNavigateBack = {}, onNavigateTo = {})
}
