package com.toritark.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.toritark.app.presentation.main.app.AppTheme
import com.toritark.app.presentation.main.app.MainApp

@Composable
fun AndroidAppView() {
    AppTheme {
        MainApp()
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AndroidAppView()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    AndroidAppView()
}