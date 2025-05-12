package com.toritark.stories.presentation.story.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.toritark.stories.data.story.model.story.story.StoryApiModel
import com.toritark.stories.presentation.core_ui.nav.NavDestination
import com.toritark.stories.presentation.core_ui.nav.OnNavigateTo
import com.toritark.stories.presentation.core_ui.nav.OnPopBackStack
import com.toritark.stories.presentation.core_ui.nav.navTypeOf
import com.toritark.stories.presentation.story.detail.StoryDetailScreen
import com.toritark.stories.presentation.story.quiz.StoryQuizScreen
import com.toritark.stories.presentation.story.text.StoryTextScreen
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
sealed interface StoryNavDestination : NavDestination {

    @Serializable
    data object Detail : StoryNavDestination

    @Serializable
    data class Text(
        val story: StoryApiModel,
    ) : StoryNavDestination

    @Serializable
    data class Quiz(
        val story: StoryApiModel,
    ) : StoryNavDestination
}

fun NavGraphBuilder.storiesScreens(onNavigateTo: OnNavigateTo, onPopBackStack: OnPopBackStack) {
    composable<StoryNavDestination.Detail> {
        StoryDetailScreen(onNavigateTo)
    }

    composable<StoryNavDestination.Text>(
        typeMap = mapOf(
            typeOf<StoryApiModel>() to navTypeOf<StoryApiModel>(),
        )
    ) {
        val route = it.toRoute<StoryNavDestination.Text>()

        StoryTextScreen(
            onNavigateTo = onNavigateTo,
            onPopBackStack = onPopBackStack,
            story = route.story,
        )
    }

    composable<StoryNavDestination.Quiz>(
        typeMap = mapOf(
            typeOf<StoryApiModel>() to navTypeOf<StoryApiModel>(),
        )
    ) {
        val route = it.toRoute<StoryNavDestination.Quiz>()

        StoryQuizScreen(
            onNavigateTo = onNavigateTo,
            onPopBackStack = onPopBackStack,
            story = route.story,
        )
    }
}
