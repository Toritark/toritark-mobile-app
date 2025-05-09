package com.toritark.stories.presentation.language.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.toritark.stories.presentation.core_ui.nav.NavDestination
import com.toritark.stories.presentation.core_ui.nav.OnNavigateTo
import com.toritark.stories.presentation.core_ui.nav.OnPopBackStack
import com.toritark.stories.presentation.language.setup.learning.LearningLanguageChooserScreen
import kotlinx.serialization.Serializable

@Serializable
data object LearningLanguageChooserScreenDestination : NavDestination

fun NavGraphBuilder.languagesScreens(
    onNavigate: OnNavigateTo,
    onPopBackStack: OnPopBackStack,
) {
    composable<LearningLanguageChooserScreenDestination> {
        LearningLanguageChooserScreen(onNavigate, onPopBackStack)
    }
}