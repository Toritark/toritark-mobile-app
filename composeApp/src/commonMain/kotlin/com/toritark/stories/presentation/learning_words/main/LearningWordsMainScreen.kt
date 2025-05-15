package com.toritark.stories.presentation.learning_words.main

import androidx.compose.runtime.Composable
import com.toritark.stories.presentation.core_ui.nav.OnNavigateTo
import com.toritark.stories.presentation.core_ui.nav.OnPopBackStack
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun LearningWordsMainScreen(
    onNavigateTo: OnNavigateTo,
    onPopBackStack: OnPopBackStack,
    viewModel: LearningWordsMainViewModel = koinViewModel(),
) {
    viewModel.onNavigateTo = onNavigateTo
    viewModel.onPopBackStack = onPopBackStack

    // TODO
}