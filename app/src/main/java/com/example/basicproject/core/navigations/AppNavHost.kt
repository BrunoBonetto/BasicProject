package com.example.basicproject.core.navigations

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.basicproject.login.navigation.authGraph
import com.example.basicproject.home.navigation.homeGraph
import com.example.basicproject.login.navigation.LoginNavRoutes
import com.example.basicproject.sessions.navigation.SessionNavRoutes

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = SessionNavRoutes.Root.route
    ) {
        navigation(
            route = SessionNavRoutes.Root.route,
            startDestination = LoginNavRoutes.Login.route
        ){
            authGraph(navController)
            homeGraph(navController)
        }
    }
}