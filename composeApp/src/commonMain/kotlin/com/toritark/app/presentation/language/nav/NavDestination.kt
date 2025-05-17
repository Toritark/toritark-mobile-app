package com.toritark.app.presentation.language.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.toritark.app.presentation.core_ui.nav.NavDestination
import com.toritark.app.presentation.core_ui.nav.OnNavigateTo
import com.toritark.app.presentation.core_ui.nav.OnPopBackStack
import com.toritark.app.presentation.language.setup.learning.LearningLanguageChooserScreen
import com.toritark.app.presentation.language.setup.level.LanguageLevelChooserScreen
import com.toritark.app.presentation.language.setup.native.NativeLanguageChooserScreen
import kotlinx.serialization.Serializable

@Serializable
sealed interface LanguageSetupScreenDestination : NavDestination {

    @Serializable
    data object LearningLanguageChooser : LanguageSetupScreenDestination

    @Serializable
    data object LanguageLevelChooser : LanguageSetupScreenDestination

    @Serializable
    data object NativeLanguageChooser : LanguageSetupScreenDestination
}


fun NavGraphBuilder.languagesScreens(
    onNavigateTo: OnNavigateTo,
    onPopBackStack: OnPopBackStack,
) {
    composable<LanguageSetupScreenDestination.LearningLanguageChooser> {
        LearningLanguageChooserScreen(onNavigateTo, onPopBackStack)
    }

    composable<LanguageSetupScreenDestination.LanguageLevelChooser> {
        LanguageLevelChooserScreen(onNavigateTo, onPopBackStack)
    }

    composable<LanguageSetupScreenDestination.NativeLanguageChooser> {
        NativeLanguageChooserScreen(onNavigateTo, onPopBackStack)
    }
}