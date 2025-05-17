package com.toritark.app.presentation.auth.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.toritark.app.presentation.auth.sign_in.SignInScreen
import com.toritark.app.presentation.core_ui.nav.NavDestination
import com.toritark.app.presentation.core_ui.nav.OnNavigateTo
import com.toritark.app.presentation.core_ui.nav.OnPopBackStack
import kotlinx.serialization.Serializable

@Serializable
sealed interface AuthScreenDestination : NavDestination {

    @Serializable
    data object SignIn : AuthScreenDestination
}

fun NavGraphBuilder.authScreens(
    onNavigateTo: OnNavigateTo,
    onPopBackStack: OnPopBackStack,
) {
    composable<AuthScreenDestination.SignIn> {
        SignInScreen(onNavigateTo = onNavigateTo, onPopBackStack = onPopBackStack)
    }
}