package com.toritark.app.presentation.splash.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.toritark.app.presentation.core_ui.nav.NavDestination
import com.toritark.app.presentation.core_ui.nav.OnNavigateTo
import com.toritark.app.presentation.splash.SplashScreen
import kotlinx.serialization.Serializable

@Serializable
data object SplashScreenDestination : NavDestination

fun NavGraphBuilder.splashScreen(onNavigateTo: OnNavigateTo) {
    composable<SplashScreenDestination> {
        SplashScreen(onNavigateTo)
    }
}