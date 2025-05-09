package com.toritark.stories.presentation.main.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import co.touchlab.kermit.Logger
import com.toritark.stories.presentation.core_ui.nav.OnNavigateTo
import com.toritark.stories.presentation.core_ui.nav.OnPopBackStack
import com.toritark.stories.presentation.language.nav.languagesScreens
import com.toritark.stories.presentation.onboarding.nav.onboardingScreens
import com.toritark.stories.presentation.splash.nav.SplashScreenDestination
import com.toritark.stories.presentation.splash.nav.splashScreen

private const val LOG_TAG = "AppNavigation"
private val logger = Logger.withTag(LOG_TAG)

@Composable
internal fun AppNavigation(

) {
    val navController = rememberNavController()

    val defaultOnNavigateTo: OnNavigateTo = { destination, optionsBuilder ->
        navController.navigate(destination, navOptions(optionsBuilder))
    }

    val defaultOnPopBackStack: OnPopBackStack = {
        navController.popBackStack()
    }

    NavHost(
        navController = navController,
        startDestination = SplashScreenDestination,
    ) {
        splashScreen(onNavigate = defaultOnNavigateTo)
        languagesScreens(onNavigate = defaultOnNavigateTo, onPopBackStack = defaultOnPopBackStack)
        onboardingScreens(onNavigate = defaultOnNavigateTo)
        mainScreen(onNavigate = defaultOnNavigateTo)
    }
}