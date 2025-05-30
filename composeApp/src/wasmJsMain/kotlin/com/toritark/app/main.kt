package com.toritark.app

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.bindToNavigation
import com.toritark.app.di.configureModules
import com.toritark.app.presentation.main.app.MainApp
import kotlinx.browser.document
import kotlinx.browser.window
import org.koin.core.context.GlobalContext.startKoin

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalBrowserHistoryApi
fun main() {
    ComposeViewport(requireNotNull(document.body)) {
        startKoin {
            configureModules()
        }
        MainApp(
//            onNavHostReady = { window.bindToNavigation(it) }
        )
    }
}