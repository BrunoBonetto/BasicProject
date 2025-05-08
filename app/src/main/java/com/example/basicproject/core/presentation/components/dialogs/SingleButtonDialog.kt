package com.example.basicproject.core.presentation.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SingleButtonDialog(
    title: String,
    message: String,
    buttonText: String,
    onButtonClick: () -> Unit
){
    AlertDialog(
        onDismissRequest = onButtonClick,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(onClick = onButtonClick,) {
                Text(buttonText)
            }
        }
    )
}