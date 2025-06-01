package com.toritark.app.presentation.main.nav

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import co.touchlab.kermit.Logger
import com.toritark.app.presentation.auth.nav.authScreens
import com.toritark.app.presentation.billing.nav.billingScreens
import com.toritark.app.presentation.core_ui.nav.OnNavigateTo
import com.toritark.app.presentation.core_ui.nav.OnPopBackStack
import com.toritark.app.presentation.language.nav.languagesScreens
import com.toritark.app.presentation.onboarding.nav.onboardingScreens
import com.toritark.app.presentation.splash.nav.SplashScreenDestination
import com.toritark.app.presentation.splash.nav.splashScreen
import com.toritark.app.presentation.story.nav.storiesScreens

private const val LOG_TAG = "AppNavigation"
private val logger = Logger.withTag(LOG_TAG)

@Composable
internal fun AppNavigation(
    onNavHostReady: suspend (NavController) -> Unit,
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
        enterTransition = { fadeIn(animationSpec = tween()) },
        exitTransition = { fadeOut(animationSpec = tween()) },
    ) {
        splashScreen(onNavigateTo = defaultOnNavigateTo)
        languagesScreens(onNavigateTo = defaultOnNavigateTo, onPopBackStack = defaultOnPopBackStack)
        onboardingScreens(onNavigateTo = defaultOnNavigateTo)
        authScreens(onNavigateTo = defaultOnNavigateTo, onPopBackStack = defaultOnPopBackStack)
        storiesScreens(onNavigateTo = defaultOnNavigateTo, onPopBackStack = defaultOnPopBackStack)
        billingScreens(onNavigateTo = defaultOnNavigateTo, onPopBackStack = defaultOnPopBackStack)
        mainScreen(onNavigateTo = defaultOnNavigateTo, onPopBackStack = defaultOnPopBackStack)
    }

    LaunchedEffect(navController) {
        onNavHostReady(navController)
    }
}