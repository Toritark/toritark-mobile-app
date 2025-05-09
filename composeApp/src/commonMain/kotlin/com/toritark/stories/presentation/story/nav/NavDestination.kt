package com.toritark.stories.presentation.story.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.toritark.stories.presentation.core_ui.nav.NavDestination
import com.toritark.stories.presentation.core_ui.nav.OnNavigateTo
import com.toritark.stories.presentation.story.detail.StoryDetailScreen
import kotlinx.serialization.Serializable

@Serializable
sealed interface StoryNavDestination : NavDestination {

    @Serializable
    data object Detail : StoryNavDestination
}

fun NavGraphBuilder.storiesScreens(onNavigate: OnNavigateTo) {
    composable<StoryNavDestination.Detail> {
        StoryDetailScreen(onNavigate)
    }
}