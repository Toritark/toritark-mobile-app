package com.toritark.stories.presentation.main.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import co.touchlab.kermit.Logger
import com.toritark.stories.presentation.splash.nav.SplashScreenDestination
import com.toritark.stories.presentation.splash.nav.splashScreen

private const val LOG_TAG = "AppNavigation"
private val logger = Logger.withTag(LOG_TAG)

@Composable
internal fun AppNavigation(

) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SplashScreenDestination,
    ) {
        splashScreen { destination, optionsBuilder ->
            navController.navigate(destination, navOptions(optionsBuilder))
        }
    }
}