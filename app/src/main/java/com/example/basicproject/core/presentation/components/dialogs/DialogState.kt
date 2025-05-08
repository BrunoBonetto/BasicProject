package com.example.basicproject.core.presentation.components.dialogs

sealed class DialogState {
    object Hidden : DialogState()
    data class Show(val tittle: String, val message: String, val buttonText: String) : DialogState()
}