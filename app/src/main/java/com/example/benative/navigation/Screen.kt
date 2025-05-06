package com.example.benative.navigation

sealed class Screen(val route: String){

    data object SplashScreen : Screen("splash_screen")

    data object SignInScreen : Screen("signIn_screen")

    data object SignUpScreen : Screen("signUp_screen")

    data object MainScreen : Screen("main_screen")

    data object LessonScreen : Screen("lesson_screen")

    data class TasksScreen(val lessonId: Int) : Screen("tasks_screen/$lessonId") {
        companion object {
            const val ROUTE = "tasks_screen/{lessonId}"
        }
    }

    data object StatsScreen : Screen("stats_screen")

    data object ProfileScreen : Screen("profile_screen")
}