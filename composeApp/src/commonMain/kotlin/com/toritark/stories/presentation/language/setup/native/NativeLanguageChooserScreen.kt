package com.toritark.stories.presentation.language.setup.native

import androidx.compose.runtime.Composable
import com.toritark.stories.presentation.core_ui.nav.OnNavigateTo
import com.toritark.stories.presentation.core_ui.nav.OnPopBackStack
import com.toritark.stories.presentation.language.setup.base.language.BaseLanguageChooserScreen
import org.koin.compose.viewmodel.koinViewModel
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_choose_native_language_next_btn
import toritark.composeapp.generated.resources.title_choose_native_language_screen

@Composable
internal fun NativeLanguageChooserScreen(
    onNavigateTo: OnNavigateTo,
    onPopBackStack: OnPopBackStack,
    viewModel: NativeLanguageChooserViewModel = koinViewModel(),
) {
    viewModel.onNavigateTo = onNavigateTo
    viewModel.onPopBackStack = onPopBackStack

    BaseLanguageChooserScreen(
        titleStringResource = Res.string.title_choose_native_language_screen,
        nextButtonStringResource = Res.string.title_choose_native_language_next_btn,
        viewModel = viewModel,
    )
}
