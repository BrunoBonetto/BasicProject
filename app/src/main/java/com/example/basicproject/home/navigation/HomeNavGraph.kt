package com.example.basicproject.home.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.basicproject.home.ui.HomeScreen
import com.example.basicproject.home.ui.HomeViewModel
import com.example.basicproject.main.navigations.SessionNavRoutes
import com.example.basicproject.product.ui.ProductDetailScreen
import com.example.basicproject.user.ui.SharedUserViewModel

fun NavGraphBuilder.homeGraph(navController: NavController) {

    composable(HomeNavRoutes.Home.route) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(SessionNavRoutes.Root.route)
        }

        HomeScreen(
            viewModel = hiltViewModel<HomeViewModel>(),
            sharedUserViewModel = hiltViewModel<SharedUserViewModel>(parentEntry),
            onNavigateToLogin = {
                navController.navigate(SessionNavRoutes.Root.route)
            },
            onNavigateToProductDetail = { productId ->
                navController.navigate(HomeNavRoutes.ProductDetail.routeWithArgs(productId))
            }
        )
    }

    composable(
        route = HomeNavRoutes.ProductDetail.route,
        arguments = listOf(navArgument("productId") { type = NavType.IntType })
    ) {
        ProductDetailScreen()
    }
}