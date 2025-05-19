package com.example.basicproject.home.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.basicproject.user.presentation.SharedUserViewModel
import com.example.basicproject.home.ui.HomeScreen
import com.example.basicproject.login.navigation.LoginNavRoutes
import com.example.basicproject.main.navigations.SessionNavRoutes

fun NavGraphBuilder.homeGraph(navController: NavController) {

    composable(HomeNavRoutes.Home.route) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(SessionNavRoutes.Root.route)
        }

        val sharedUserViewModel = hiltViewModel<SharedUserViewModel>(parentEntry)

        HomeScreen(
            sharedUserViewModel = sharedUserViewModel,
            onNavigateToLogin = {
                navController.navigate(LoginNavRoutes.Login.route) {
                    popUpTo(LoginNavRoutes.Login.route) { inclusive = true }
                }
            }
        )
    }

    /*
    composable(NavRoutes.Profile.route) {
        val profileViewModel: ProfileViewModel = hiltViewModel()
        ProfileScreen(viewModel = profileViewModel)
    }
     */
}