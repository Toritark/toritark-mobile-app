@file:OptIn(ExperimentalMaterial3Api::class)

package com.toritark.stories.presentation.main.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.Checklist
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.toritark.stories.presentation.core_ui.nav.OnNavigateTo
import com.toritark.stories.presentation.core_ui.nav.OnPopBackStack
import com.toritark.stories.presentation.learning_words.main.LearningWordsMainScreen
import com.toritark.stories.presentation.main.nav.MainScreenDestination
import com.toritark.stories.presentation.profile.main.ProfileMainScreen
import com.toritark.stories.presentation.story.detail.StoryDetailScreen
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import toritark.composeapp.generated.resources.*

@Composable
internal fun MainScreen(
    destination: MainScreenDestination,
    onNavigateTo: OnNavigateTo,
    onPopBackStack: OnPopBackStack,
    viewModel: MainViewModel = koinViewModel(),
) {
    viewModel.onNavigateTo = onNavigateTo

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                title = {
                    Text(stringResource(Res.string.title_main_screen_topbar))
                },
                actions = {

                },
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentDestination = destination,
                onNavigateTo = onNavigateTo,
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            when (destination) {
                MainScreenDestination.Story -> {
                    StoryDetailScreen(
                        onNavigateTo = onNavigateTo,
                        onPopBackStack = onPopBackStack,
                    )
                }

                MainScreenDestination.LearningWords -> {
                    LearningWordsMainScreen(
                        onNavigateTo = onNavigateTo,
                        onPopBackStack = onPopBackStack,
                    )
                }

                MainScreenDestination.Profile -> {
                    ProfileMainScreen(
                        onNavigateTo = onNavigateTo,
                        onPopBackStack = onPopBackStack,
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomNavigationBar(
    currentDestination: MainScreenDestination,
    onNavigateTo: OnNavigateTo,
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        BottomNavigationBarItem(
            currentDestination = currentDestination,
            targetDestination = MainScreenDestination.Story,
            labelText = stringResource(Res.string.title_main_bottom_nav_story),
            icon = Icons.Rounded.Book,
            onNavigateTo = onNavigateTo,
        )

        BottomNavigationBarItem(
            currentDestination = currentDestination,
            targetDestination = MainScreenDestination.LearningWords,
            labelText = stringResource(Res.string.title_main_bottom_nav_learning_words),
            icon = Icons.Rounded.Checklist,
            onNavigateTo = onNavigateTo,
        )

        BottomNavigationBarItem(
            currentDestination = currentDestination,
            targetDestination = MainScreenDestination.Profile,
            labelText = stringResource(Res.string.title_main_bottom_nav_profile),
            icon = Icons.Rounded.Person,
            onNavigateTo = onNavigateTo,
        )
    }
}

@Composable
private fun RowScope.BottomNavigationBarItem(
    currentDestination: MainScreenDestination,
    targetDestination: MainScreenDestination,
    labelText: String,
    icon: ImageVector,
    onNavigateTo: OnNavigateTo,
) {
    val hapticFeedback = LocalHapticFeedback.current

    NavigationBarItem(
        selected = currentDestination == targetDestination,
        colors = NavigationBarItemDefaults.colors(
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,

            selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
            selectedTextColor = MaterialTheme.colorScheme.onSurface,

            indicatorColor = MaterialTheme.colorScheme.secondary,
        ),
        onClick = {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)

            if (currentDestination != targetDestination) {
                onNavigateTo(targetDestination) {}
            }
        },
        label = {
            Text(
                text = labelText,
                color = MaterialTheme.colorScheme.onBackground,
            )
        },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = labelText,
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
    )
}