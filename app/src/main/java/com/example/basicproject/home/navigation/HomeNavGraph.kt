package com.example.basicproject.home.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.basicproject.core.shared.SharedUserViewModel
import com.example.basicproject.home.ui.HomeScreen
import com.example.basicproject.sessions.navigation.SessionNavRoutes

fun NavGraphBuilder.homeGraph(navController: NavController) {
    composable(HomeNavRoutes.Home.route) {backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(SessionNavRoutes.Root.route)
        }

        val sharedUserViewModel = hiltViewModel<SharedUserViewModel>(parentEntry)

        HomeScreen(sharedUserViewModel)
    }

    /*
    composable(NavRoutes.Profile.route) {
        val profileViewModel: ProfileViewModel = hiltViewModel()
        ProfileScreen(viewModel = profileViewModel)
    }
     */
}