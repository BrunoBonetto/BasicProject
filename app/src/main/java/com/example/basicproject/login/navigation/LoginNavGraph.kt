package com.example.basicproject.login.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.basicproject.home.navigation.HomeNavRoutes
import com.example.basicproject.login.ui.LoginScreen
import com.example.basicproject.login.ui.LoginViewModel
import com.example.basicproject.main.navigations.SessionNavRoutes
import com.example.basicproject.splash.navigation.SplashNavRoutes
import com.example.basicproject.splash.ui.SplashScreen
import com.example.basicproject.splash.ui.SplashViewModel
import com.example.basicproject.user.ui.SharedUserViewModel

fun NavGraphBuilder.authGraph(navController: NavController) {

    composable(SplashNavRoutes.Splash.route) { backStackEntry ->

        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(SessionNavRoutes.Root.route)
        }

        SplashScreen(
            viewModel = hiltViewModel<SplashViewModel>(),
            sharedUserViewModel = hiltViewModel<SharedUserViewModel>(parentEntry),
            onNavigateToLogin = {
                navController.navigate(LoginNavRoutes.Login.route) {
                    popUpTo(LoginNavRoutes.Login.route) { inclusive = true }
                }
            },
            onNavigateToHome = {
                navController.navigate(HomeNavRoutes.Home.route) {
                    popUpTo(HomeNavRoutes.Home.route) { inclusive = true }
                }
            }
        )
    }

    composable(LoginNavRoutes.Login.route) { backStackEntry ->

        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(SessionNavRoutes.Root.route)
        }

        LoginScreen(
            viewModel = hiltViewModel<LoginViewModel>(),
            sharedUserViewModel = hiltViewModel<SharedUserViewModel>(parentEntry),
            onNavigateToHome = {
                navController.navigate(HomeNavRoutes.Home.route) {
                    popUpTo(LoginNavRoutes.Login.route) { inclusive = true }
                }
            }
        )
    }
}