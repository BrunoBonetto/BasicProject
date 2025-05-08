package com.example.basicproject.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.basicproject.R
import com.example.basicproject.core.shared.SharedUserViewModel
import com.example.basicproject.user.domain.model.UserState

@Composable
fun HomeScreen(sharedUserViewModel: SharedUserViewModel) {

    val userState by remember { derivedStateOf { sharedUserViewModel.userState } }

    when (userState) {
        is UserState.LoggedIn -> {
            val user = (userState as UserState.LoggedIn).user
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = stringResource(R.string.welcome_user, user.userName),
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.email, user.email),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        UserState.NotLoggedIn -> {
            Text("Usuário não autenticado.")
        }
    }


}