package com.example.benative.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.benative.presentation.screen.CreditsScreen
import com.example.benative.presentation.screen.LessonScreen
import com.example.benative.presentation.screen.MainScreen
import com.example.benative.presentation.screen.ProfileScreen
import com.example.benative.presentation.screen.SignInScreen
import com.example.benative.presentation.screen.SignUpScreen
import com.example.benative.presentation.screen.SplashScreen
import com.example.benative.presentation.screen.StatsScreen
import com.example.benative.presentation.screen.TaskScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.SignInScreen.route) {

        composable(route = Screen.SignInScreen.route){
            SignInScreen{ onNavigateTo ->
                navController.navigate(onNavigateTo.route){
                launchSingleTop = true
                }
            }
        }

        composable(route = Screen.SplashScreen.route){
            SplashScreen{ onNavigateTo ->
                navController.navigate(onNavigateTo.route){
                    launchSingleTop = true
                }
            }
        }

        composable(route = Screen.SignUpScreen.route){
            SignUpScreen(
                onNavigateBack = { navController.popBackStack() }
            ) { onNavigateTo ->
                navController.navigate(onNavigateTo.route) {
                    launchSingleTop = true
                }
            }
        }

        composable(route = Screen.MainScreen.route){
            MainScreen{ onNavigateTo ->
                navController.navigate(onNavigateTo.route){
                    launchSingleTop = true
                }
            }
        }

        composable(route = Screen.LessonScreen.route){
            LessonScreen{ onNavigateTo ->
                navController.navigate(onNavigateTo.route){
                    launchSingleTop = true
                }
            }
        }

        composable(
            route = Screen.TasksScreen.ROUTE,
            arguments = listOf(navArgument("lessonId") { type = NavType.IntType })
        ) { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getInt("lessonId") ?: 0
            TaskScreen(
                lessonId = lessonId,
                onNavigateBack = { navController.popBackStack() },
                onTaskChecked = { task, isCorrect ->
                    // Обработка проверки
                }
            )
        }

        composable(route = Screen.StatsScreen.route){
            StatsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.ProfileScreen.route){
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() }
            ){ onNavigateTo ->
                navController.navigate(onNavigateTo.route){
                    launchSingleTop = true
                }
            }
        }

        composable(route = Screen.CreditsScreen.route){
            CreditsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

