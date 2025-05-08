package com.example.basicproject.login.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.basicproject.core.shared.SharedUserViewModel
import com.example.basicproject.login.ui.LoginScreen
import com.example.basicproject.home.navigation.HomeNavRoutes
import com.example.basicproject.sessions.navigation.SessionNavRoutes

fun NavGraphBuilder.authGraph(navController: NavController) {
    composable(LoginNavRoutes.Login.route) { backStackEntry ->

        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(SessionNavRoutes.Root.route)
        }
        val sharedUserViewModel = hiltViewModel<SharedUserViewModel>(parentEntry)

        LoginScreen (
            sharedUserViewModel = sharedUserViewModel,
            onNavigateToHome = {
                navController.navigate(HomeNavRoutes.Home.route) {
                    popUpTo(LoginNavRoutes.Login.route) { inclusive = true }
                }
            }
        )
    }
}