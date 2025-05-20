package com.toritark.app.presentation.main.app

import androidx.compose.runtime.Composable
import co.touchlab.kermit.Logger
import com.toritark.app.presentation.main.app.theme.AppTheme
import com.toritark.app.presentation.main.nav.AppNavigation
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val LOG_TAG = "MainApp"
private val logger = Logger.withTag(LOG_TAG)

@Composable
@Preview
fun MainApp() {
    AppTheme {
        AppNavigation()
    }
}