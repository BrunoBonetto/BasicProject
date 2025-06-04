package com.example.basicproject.home.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.basicproject.home.ui.state.HomeIntent
import com.example.basicproject.user.ui.SharedUserViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    sharedUserViewModel: SharedUserViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToProductDetail: (Int) -> Unit
) {

    val currentUserState by sharedUserViewModel.currentUserState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    BackHandler(enabled = true) {}

    LaunchedEffect(Unit) {
        viewModel.onIntent(HomeIntent.LoadProducts)
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is HomeUiEvent.LogoutSuccess -> {
                    onNavigateToLogin()
                    sharedUserViewModel.clearUser()
                }

                is HomeUiEvent.LogoutError -> {}
            }
        }
    }

    HomeScreenContent(
        userState = currentUserState,
        homeUiState = uiState,
        onLogoutClick = { viewModel.onIntent(HomeIntent.Logout) },
        onProductClick = onNavigateToProductDetail
    )
}