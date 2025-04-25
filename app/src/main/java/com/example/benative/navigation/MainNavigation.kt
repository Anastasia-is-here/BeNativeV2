package com.example.benative.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.benative.ui.screen.SignInScreen
import com.example.benative.ui.screen.SignUpScreen
import com.example.benative.ui.screen.SplashScreen

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
            SignUpScreen{ onNavigateTo ->
                navController.navigate(onNavigateTo.route){
                    launchSingleTop = true
                }
            }
        }
    }
}

