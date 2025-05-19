package com.example.basicproject.splash.ui.state

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.basicproject.R
import com.example.basicproject.core.presentation.components.dialogs.DialogState
import com.example.basicproject.core.presentation.components.dialogs.SingleButtonDialog

@Composable
fun SplashScreenContent(
    dialogState: DialogState,
    onDismissDialog: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colorResource(R.color.purple_500),
                        colorResource(R.color.purple_200)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }

    if (dialogState is DialogState.Show) {
        SingleButtonDialog(
            title = dialogState.tittle,
            message = dialogState.message,
            buttonText = dialogState.buttonText,
            onButtonClick = onDismissDialog
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreenContent(
        dialogState = DialogState.Hidden,
        onDismissDialog = {}
    )
}