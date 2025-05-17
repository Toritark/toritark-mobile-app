package com.toritark.app.presentation.story.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.toritark.app.data.story.model.retelling.retelling.StoryRetellingReviewApiModel
import com.toritark.app.data.story.model.story.story.StoryApiModel
import com.toritark.app.presentation.core_ui.nav.NavDestination
import com.toritark.app.presentation.core_ui.nav.OnNavigateTo
import com.toritark.app.presentation.core_ui.nav.OnPopBackStack
import com.toritark.app.presentation.core_ui.nav.navTypeOf
import com.toritark.app.presentation.story.detail.StoryDetailScreen
import com.toritark.app.presentation.story.quiz.StoryQuizScreen
import com.toritark.app.presentation.story.retelling.detail.StoryRetellingDetailScreen
import com.toritark.app.presentation.story.text.StoryTextScreen
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

    @Serializable
    data class RetellingReviewDetail(
        val review: StoryRetellingReviewApiModel,
    ) : StoryNavDestination
}

fun NavGraphBuilder.storiesScreens(onNavigateTo: OnNavigateTo, onPopBackStack: OnPopBackStack) {
    composable<StoryNavDestination.Detail> {
        StoryDetailScreen(onNavigateTo = onNavigateTo, onPopBackStack = onPopBackStack)
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

    composable<StoryNavDestination.RetellingReviewDetail>(
        typeMap = mapOf(
            typeOf<StoryRetellingReviewApiModel>() to navTypeOf<StoryRetellingReviewApiModel>(),
        )
    ) {
        val route = it.toRoute<StoryNavDestination.RetellingReviewDetail>()

        StoryRetellingDetailScreen(
            review = route.review,
            onNavigateTo = onNavigateTo,
            onPopBackStack = onPopBackStack,
        )
    }
}
