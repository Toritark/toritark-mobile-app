package com.toritark.stories.presentation.language.setup.learning

import androidx.compose.runtime.Composable
import com.toritark.stories.presentation.core_ui.nav.OnNavigateTo
import com.toritark.stories.presentation.core_ui.nav.OnPopBackStack
import com.toritark.stories.presentation.language.setup.base.language.BaseLanguageChooserScreen
import org.koin.compose.viewmodel.koinViewModel
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_choose_learning_language_next_btn
import toritark.composeapp.generated.resources.title_choose_learning_language_screen

@Composable
internal fun LearningLanguageChooserScreen(
    onNavigateTo: OnNavigateTo,
    onPopBackStack: OnPopBackStack,
    viewModel: LearningLanguageChooserViewModel = koinViewModel(),
) {
    viewModel.onNavigateTo = onNavigateTo
    viewModel.onPopBackStack = onPopBackStack

    BaseLanguageChooserScreen(
        titleStringResource = Res.string.title_choose_learning_language_screen,
        nextButtonStringResource = Res.string.title_choose_learning_language_next_btn,
        viewModel = viewModel,
    )
}
