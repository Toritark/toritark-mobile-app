package com.toritark.stories.presentation.onboarding.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.toritark.stories.presentation.core_ui.nav.NavDestination
import com.toritark.stories.presentation.core_ui.nav.OnNavigateTo
import com.toritark.stories.presentation.onboarding.main.OnboardingMainScreen
import kotlinx.serialization.Serializable

@Serializable
data object OnboardingMainScreenDestination : NavDestination

fun NavGraphBuilder.onboardingScreens(onNavigateTo: OnNavigateTo) {
    composable<OnboardingMainScreenDestination> {
        OnboardingMainScreen(onNavigateTo)
    }
}