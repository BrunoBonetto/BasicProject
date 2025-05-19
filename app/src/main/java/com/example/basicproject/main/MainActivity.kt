package com.example.basicproject.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.basicproject.main.navigations.AppNavHost
import com.example.basicproject.core.theme.BasicProjectTheme
import com.example.basicproject.main.extensions.hideStatusBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBar()
        enableEdgeToEdge()
        setContent {
            BasicProjectTheme {
                AppNavHost()
            }
        }
    }
}
