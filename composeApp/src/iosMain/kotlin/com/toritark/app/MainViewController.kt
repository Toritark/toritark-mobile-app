package com.toritark.app

import androidx.compose.ui.uikit.LocalUIViewController
import androidx.compose.ui.window.ComposeUIViewController
import com.toritark.app.domain.ads.provider.AdsProvider
import com.toritark.app.domain.ads.provider.IOSAdsProvider
import com.toritark.app.presentation.main.app.MainApp
import com.toritark.app.presentation.main.app.theme.AppTheme
import org.koin.compose.getKoin

@Suppress("FunctionName", "unused")
fun MainViewController() = ComposeUIViewController {
    val adsProvider = getKoin().get<AdsProvider>() as IOSAdsProvider
    adsProvider.setViewController(LocalUIViewController.current)

    AppTheme {
        MainApp()
    }
}