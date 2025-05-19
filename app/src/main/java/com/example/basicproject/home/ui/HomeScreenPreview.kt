package com.example.basicproject.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.basicproject.R
import com.example.basicproject.user.data.remote.entity.UserEntity
import com.example.basicproject.user.domain.model.UserState

@Composable
fun HomeScreenContent(
    userState: UserState,
    onLogoutClick: () -> Unit
) {
    when (userState) {
        is UserState.LoggedIn -> {
            val user = (userState).user
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                ) {
                    Text(
                        text = stringResource(R.string.welcome_user, user.userName),
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.email, user.email),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = onLogoutClick) {
                        Text(stringResource(R.string.logout))
                    }
                }
            }
        }

        UserState.NotLoggedIn -> {
            Text("Usuário não autenticado.")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreenContent(
        userState = UserState.LoggedIn(
            user = UserEntity(
                1,
                "John Doe",
                "john@example.com",
            )
        ),
        onLogoutClick = {}
    )
}