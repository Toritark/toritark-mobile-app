package com.toritark.stories.presentation.splash.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.toritark.stories.presentation.core_ui.nav.NavDestination
import com.toritark.stories.presentation.core_ui.nav.OnNavigateTo
import com.toritark.stories.presentation.splash.SplashScreen
import kotlinx.serialization.Serializable

@Serializable
data object SplashScreenDestination : NavDestination

fun NavGraphBuilder.splashScreen(onNavigate: OnNavigateTo) {
    composable<SplashScreenDestination> {
        SplashScreen(onNavigate)
    }
}