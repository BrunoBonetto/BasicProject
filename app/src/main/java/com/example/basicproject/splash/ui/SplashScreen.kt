package com.example.basicproject.splash.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.basicproject.R
import com.example.basicproject.core.presentation.components.dialogs.DialogState
import com.example.basicproject.splash.ui.state.SplashScreenContent
import com.example.basicproject.user.ui.SharedUserViewModel
import com.example.basicproject.user.ui.state.CurrentUserState

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    sharedUserViewModel: SharedUserViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val currentUserState by viewModel.currentUserState.collectAsState()
    val context = LocalContext.current
    var dialogState by remember { mutableStateOf<DialogState>(DialogState.Hidden) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is SplashUiEvent.GoToLogin -> onNavigateToLogin()
                is SplashUiEvent.GoToHome -> onNavigateToHome()
                is SplashUiEvent.Error -> dialogState = DialogState.Show(
                    tittle = context.getString(R.string.error_title_server),
                    message = context.getString(R.string.error_body_server),
                    buttonText = context.getString(R.string.ok)
                )
            }
        }
    }

    LaunchedEffect(currentUserState) {
        when (currentUserState) {
            is CurrentUserState.Success -> {
                sharedUserViewModel.setUser((currentUserState as CurrentUserState.Success).user)
            }
            //Below states are not really necessary right now, but are implemented for future scalability
            is CurrentUserState.Loading -> Unit
            is CurrentUserState.Unloaded -> Unit
            is CurrentUserState.Error -> Unit
        }
    }

    SplashScreenContent(
        dialogState = dialogState,
        onDismissDialog = {
            dialogState = DialogState.Hidden
            onNavigateToLogin()
        }
    )
}