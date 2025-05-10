package com.toritark.stories.presentation.main.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.toritark.stories.presentation.core_ui.nav.NavDestination
import com.toritark.stories.presentation.core_ui.nav.OnNavigateTo
import com.toritark.stories.presentation.main.screen.MainScreen
import kotlinx.serialization.Serializable

@Serializable
data object MainScreenDestination : NavDestination

fun NavGraphBuilder.mainScreen(onNavigateTo: OnNavigateTo) {
    composable<MainScreenDestination> {
        MainScreen(onNavigateTo)
    }
}