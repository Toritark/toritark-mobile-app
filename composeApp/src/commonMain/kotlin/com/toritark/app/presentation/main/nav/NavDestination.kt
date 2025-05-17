package com.toritark.app.presentation.main.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.toritark.app.presentation.core_ui.nav.NavDestination
import com.toritark.app.presentation.core_ui.nav.OnNavigateTo
import com.toritark.app.presentation.core_ui.nav.OnPopBackStack
import com.toritark.app.presentation.main.screen.MainScreen
import kotlinx.serialization.Serializable

@Serializable
sealed interface MainScreenDestination : NavDestination {

    @Serializable
    data object Story : MainScreenDestination

    @Serializable
    data object LearningWords : MainScreenDestination

    @Serializable
    data object Profile : MainScreenDestination
}

fun NavGraphBuilder.mainScreen(onNavigateTo: OnNavigateTo, onPopBackStack: OnPopBackStack) {
    mainScreenChild<MainScreenDestination.Story>(
        onNavigateTo = onNavigateTo,
        onPopBackStack = onPopBackStack,
        destination = MainScreenDestination.Story,
    )
    mainScreenChild<MainScreenDestination.LearningWords>(
        onNavigateTo = onNavigateTo,
        onPopBackStack = onPopBackStack,
        destination = MainScreenDestination.LearningWords,
    )
    mainScreenChild<MainScreenDestination.Profile>(
        onNavigateTo = onNavigateTo,
        onPopBackStack = onPopBackStack,
        destination = MainScreenDestination.Profile,
    )
}

private inline fun <reified T : MainScreenDestination> NavGraphBuilder.mainScreenChild(
    noinline onNavigateTo: OnNavigateTo,
    noinline onPopBackStack: OnPopBackStack,
    destination: T,
) {
    composable<T> { navBackStackEntry ->
        MainScreen(
            destination = destination,
            onNavigateTo = { navDestination, navOptionsBuilder ->
                if (navDestination is MainScreenDestination) {
                    onNavigateTo(navDestination) {
                        launchSingleTop = true

                        popUpTo(navDestination) {
                            inclusive = true
                        }

                        navOptionsBuilder()
                    }
                } else {
                    onNavigateTo(navDestination, navOptionsBuilder)
                }
            },
            onPopBackStack = onPopBackStack,
        )
    }
}
