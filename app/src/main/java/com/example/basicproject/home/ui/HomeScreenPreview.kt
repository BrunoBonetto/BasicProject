package com.example.basicproject.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.basicproject.R
import com.example.basicproject.home.ui.components.ProductItem
import com.example.basicproject.home.ui.state.HomeUiState
import com.example.basicproject.user.data.remote.entity.UserEntity
import com.example.basicproject.user.ui.state.CurrentUserState

@Composable
fun HomeScreenContent(
    userState: CurrentUserState,
    homeUiState: HomeUiState,
    onLogoutClick: () -> Unit,
    onProductClick: (Int) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (userState) {
            is CurrentUserState.Success -> {
                val user = userState.user

                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = stringResource(R.string.welcome_user, user.userName),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 15.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(homeUiState.products) { product ->
                            ProductItem(product = product, onClick = { onProductClick(product.id) })
                        }
                    }

                    Button(
                        onClick = onLogoutClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .navigationBarsPadding()
                    ) {
                        Text(text = stringResource(R.string.logout))
                    }
                }

                if (homeUiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            CurrentUserState.Unloaded -> {
                if (homeUiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    Text(
                        text = stringResource(R.string.user_not_authenticated),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            CurrentUserState.Error -> {
                if (homeUiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    Text(
                        text = stringResource(R.string.user_not_authenticated),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            CurrentUserState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreenContent(
        userState = CurrentUserState.Success(
            user = UserEntity(
                1,
                "John Doe",
                "john@example.com",
            )
        ),
        homeUiState = HomeUiState(isLoading = false),
        onLogoutClick = {},
        onProductClick = {}
    )
}