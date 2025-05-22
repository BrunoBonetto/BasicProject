package com.example.basicproject.login.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.basicproject.R
import com.example.basicproject.core.presentation.components.dialogs.DialogState
import com.example.basicproject.user.presentation.SharedUserViewModel
import com.example.basicproject.login.ui.state.LoginIntent
import com.example.basicproject.user.presentation.state.CurrentUserState

@Composable
fun LoginScreen(sharedUserViewModel: SharedUserViewModel, onNavigateToHome: () -> Unit) {
    val viewModel: LoginViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val currentUserState by viewModel.currentUserState.collectAsState()
    val context = LocalContext.current
    var dialogState by remember { mutableStateOf<DialogState>(DialogState.Hidden) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            dialogState = when (event) {
                is LoginUiEvent.LoginSuccess -> {
                    onNavigateToHome()
                    DialogState.Hidden
                }
                is LoginUiEvent.ShowInvalidCredentials -> DialogState.Show(
                    tittle = context.getString(R.string.error_title_invalid_credentials),
                    message = context.getString(R.string.error_body_invalid_credentials),
                    buttonText = context.getString(R.string.ok)
                )
                is LoginUiEvent.ShowEmptyResponse -> DialogState.Show(
                    tittle = context.getString(R.string.error_title_empty_response),
                    message = context.getString(R.string.error_body_empty_response),
                    buttonText = context.getString(R.string.ok)
                )
                is LoginUiEvent.ShowServerError -> DialogState.Show(
                    tittle = context.getString(R.string.error_title_server),
                    message = context.getString(R.string.error_body_server),
                    buttonText = context.getString(R.string.ok)
                )
                is LoginUiEvent.ShowLocalStorageError -> DialogState.Show(
                    tittle = context.getString(R.string.error_title_local_storage),
                    message = context.getString(R.string.error_body_local_storage),
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
            is CurrentUserState.Loading -> {

            }
            is CurrentUserState.Unloaded -> {

            }
            is CurrentUserState.Error -> {

            }
        }
    }

    LoginScreenContent(
        uiState = uiState,
        dialogState = dialogState,
        onUsernameChange = { viewModel.onIntent(LoginIntent.UserNameChanged(it)) },
        onPasswordChange = { viewModel.onIntent(LoginIntent.PasswordChanged(it)) },
        onLoginClick = { viewModel.onIntent(LoginIntent.Submit) },
        onDismissDialog = { dialogState = DialogState.Hidden }
    )
}