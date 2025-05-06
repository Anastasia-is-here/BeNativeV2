package com.example.benative.server

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String
)

@Serializable
data class LoginRequest(
    val login: String,
    val password: String
)

@Serializable
data class User(
    val id: Int,
    val name: String,
    val login: String,
    val experience: Int,
    val avatar: String?
)

@Serializable
data class Lesson(
    val id: Int,
    val title: String,
    val iconUrl: String,
    val mediaUrl: String?,
    val articleText: String?
)

@Serializable
data class Task(
    val id: Int,
    val lessonId: Int,
    val taskText: String,
    val correctAnswer: String,
    val experienceReward: Int,
    val taskType: String, // Будет сериализовано как строка ("ARTICLE", "MEDIA", "QUIZ")
    val options: String?
)

data class TaskUiState(
    val userInput: MutableState<String> = mutableStateOf(""),
    val isChecked: MutableState<Boolean> = mutableStateOf(false),
    val isCorrect: MutableState<Boolean> = mutableStateOf(false)
)

@Serializable
data class LessonCompletionRequest(
    val lessonId: Int,
    val results: List<TaskResult>
)

@Serializable
data class TaskResult(
    val taskId: Int,
    val isCompleted: Boolean,
    val earnedExp: Int
)

@Serializable
data class UserStatsResponse(
    val completedLessons: Int,
    val averageProgress: Int,
    val completedTasks: Int,
    val totalExperience: Int
)

@Serializable
data class RegisterRequest(
    val login: String,
    val password: String,
    val name: String
)

