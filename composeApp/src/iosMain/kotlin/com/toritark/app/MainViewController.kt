package com.toritark.app

import androidx.compose.ui.window.ComposeUIViewController
import com.toritark.app.presentation.main.app.MainApp
import com.toritark.app.presentation.main.app.theme.AppTheme

fun MainViewController() = ComposeUIViewController {
    AppTheme {
        MainApp()
    }
}