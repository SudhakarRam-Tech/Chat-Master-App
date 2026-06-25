package com.sk.chatmaster.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sk.chatmaster.ui.Login.LoginScreen

@Composable
fun AppNavigation() {
    // 1. Initialize the NavController
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Route.LoginScreen,
        builder = {
            composable(route = Route.LoginScreen) {
                LoginScreen(navController)
            }
        })
}
