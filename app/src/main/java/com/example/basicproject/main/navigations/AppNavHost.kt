package com.example.basicproject.main.navigations

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.basicproject.login.navigation.authGraph
import com.example.basicproject.home.navigation.homeGraph
import com.example.basicproject.splash.navigation.SplashNavRoutes

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = SessionNavRoutes.Root.route
    ) {
        navigation(
            route = SessionNavRoutes.Root.route,
            startDestination = SplashNavRoutes.Splash.route
        ){
            authGraph(navController)
            homeGraph(navController)
        }
    }
}