package com.example.basicproject.home.navigation

sealed class HomeNavRoutes(val route: String) {
    object Home: HomeNavRoutes("home")
    object ProductDetail : HomeNavRoutes("product_detail/{productId}") {
        fun routeWithArgs(productId: Int) = "product_detail/$productId"
    }
}