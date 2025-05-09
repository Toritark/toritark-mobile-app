package com.toritark.stories.presentation.core_ui.nav

import androidx.navigation.NavOptionsBuilder
import kotlinx.serialization.Serializable

interface NavDestination

@Serializable
sealed interface MainNavDestinations : NavDestination {

    @Serializable
    data object Main : MainNavDestinations
}

@Serializable
data object PreviousScreen : NavDestination


typealias OnNavigateTo = (navDestination: NavDestination, navOptionsBuilder: NavOptionsBuilder.() -> Unit) -> Unit
