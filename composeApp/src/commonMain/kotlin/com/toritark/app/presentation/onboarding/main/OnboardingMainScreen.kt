package com.toritark.app.presentation.onboarding.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import co.touchlab.kermit.Logger
import com.toritark.app.presentation.core_ui.nav.OnNavigateTo
import org.koin.compose.viewmodel.koinViewModel

private const val LOG_TAG = "OnboardingMainScreen"
private val logger = Logger.withTag(LOG_TAG)

@Composable
internal fun OnboardingMainScreen(
    onNavigateTo: OnNavigateTo,
    viewModel: OnboardingMainViewModel = koinViewModel(),
) {
    viewModel.onNavigateTo = onNavigateTo

    LaunchedEffect(Unit) {
        logger.d { "LaunchedEffect" }
        viewModel.update()
    }
}