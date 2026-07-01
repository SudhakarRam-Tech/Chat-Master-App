package com.sk.chatmaster.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sk.chatmaster.core.common.AppConfig
import com.sk.chatmaster.ui.Chat.ChatScreen
import com.sk.chatmaster.ui.chatList.ChatListScreen
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
            composable(route = Route.ChatListScreen) {
                ChatListScreen(navController)
            }
            composable(route = Route.ChatScreen,
                arguments = listOf(
                    navArgument("receiverId") { type = NavType.StringType },
                    navArgument("receiverName") { type = NavType.StringType }
                )) { backStackEntry ->
                // Extract parameters safely
                val receiverId = backStackEntry.arguments?.getString("receiverId").orEmpty()
                val receiverName = backStackEntry.arguments?.getString("receiverName").orEmpty()

                ChatScreen(
                    navController,receiverId =  receiverId,
                    receiverName = receiverName,
                )
            }
        })
}
