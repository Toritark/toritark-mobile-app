package com.toritark.stories

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.toritark.stories.presentation.main.app.AppTheme
import com.toritark.stories.presentation.main.app.MainApp

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