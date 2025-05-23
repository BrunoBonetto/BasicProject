package com.example.basicproject.home.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.basicproject.home.ui.state.HomeIntent
import com.example.basicproject.user.domain.model.UserState
import com.example.basicproject.user.presentation.SharedUserViewModel
import com.example.basicproject.user.presentation.state.CurrentUserState

@Composable
fun HomeScreen(
    sharedUserViewModel: SharedUserViewModel,
    onNavigateToLogin: () -> Unit
) {

    val viewModel: HomeViewModel = hiltViewModel()
    val currentUserState by viewModel.currentUserState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    BackHandler(enabled = true){}

    LaunchedEffect(Unit) {
        val userState = sharedUserViewModel.userState
        if (userState is UserState.LoggedIn) {
            viewModel.onIntent(HomeIntent.SetUser(userState.user))
            viewModel.onIntent(HomeIntent.LoadProducts)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is HomeUiEvent.LogoutSuccess -> {
                    onNavigateToLogin()
                }

                is HomeUiEvent.LogoutError -> {

                }
            }
        }
    }

    LaunchedEffect(currentUserState) {
        when (currentUserState) {
            is CurrentUserState.Success -> {}
            is CurrentUserState.Loading -> Unit
            CurrentUserState.Error -> Unit
            CurrentUserState.Unloaded -> {
                sharedUserViewModel.clearUser()
            }
        }
    }

    val userState = remember(currentUserState) {
        when (currentUserState) {
            is CurrentUserState.Success -> UserState.LoggedIn((currentUserState as CurrentUserState.Success).user)
            else -> UserState.NotLoggedIn
        }
    }

    HomeScreenContent(
        userState = userState,
        homeUiState = uiState,
        onLogoutClick = {
            viewModel.onIntent(HomeIntent.Logout)
        }
    )
}