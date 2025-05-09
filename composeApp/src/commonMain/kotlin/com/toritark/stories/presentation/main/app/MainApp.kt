package com.toritark.stories.presentation.main.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import co.touchlab.kermit.Logger
import com.toritark.stories.presentation.main.nav.AppNavigation
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val LOG_TAG = "MainApp"
private val logger = Logger.withTag(LOG_TAG)

@Composable
@Preview
fun MainApp() {
    MaterialTheme {
        AppNavigation()
    }
}