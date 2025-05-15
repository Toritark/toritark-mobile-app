package com.toritark.stories.presentation.profile.main

import androidx.compose.runtime.Composable
import com.toritark.stories.presentation.core_ui.nav.OnNavigateTo
import com.toritark.stories.presentation.core_ui.nav.OnPopBackStack
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun ProfileMainScreen(
    onNavigateTo: OnNavigateTo,
    onPopBackStack: OnPopBackStack,
    viewModel: ProfileMainViewModel = koinViewModel(),
) {
    // TODO: Profile Screen
    viewModel.onNavigateTo = onNavigateTo
    viewModel.onPopBackStack = onPopBackStack
}