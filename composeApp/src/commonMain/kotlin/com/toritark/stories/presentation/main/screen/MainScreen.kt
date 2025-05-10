@file:OptIn(ExperimentalMaterial3Api::class)

package com.toritark.stories.presentation.main.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.toritark.stories.presentation.core_ui.nav.OnNavigateTo
import com.toritark.stories.presentation.story.detail.StoryDetailScreen
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_main_screen_topbar

@Composable
internal fun MainScreen(
    onNavigateTo: OnNavigateTo,
    viewModel: MainViewModel = koinViewModel(),
) {
    viewModel.onNavigateTo = onNavigateTo

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(stringResource(Res.string.title_main_screen_topbar))
                },
                actions = {
                    IconButton(onClick = {
                        // TODO: Open settings screen
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = null,
                        )
                    }
                },
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            StoryDetailScreen(onNavigateTo = onNavigateTo)
        }
    }
}