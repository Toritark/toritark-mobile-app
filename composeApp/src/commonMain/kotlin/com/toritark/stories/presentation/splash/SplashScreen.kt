package com.toritark.stories.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.toritark.stories.presentation.core_ui.nav.OnNavigateTo
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
internal fun SplashScreen(
    onNavigateTo: OnNavigateTo,
    viewModel: SplashViewModel = koinViewModel(),
) {
    viewModel.onNavigateTo = onNavigateTo

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(64.dp)
                .align(Alignment.Center),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}