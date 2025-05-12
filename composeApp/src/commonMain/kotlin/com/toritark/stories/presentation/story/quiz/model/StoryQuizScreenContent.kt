package com.toritark.stories.presentation.story.quiz.model

import com.toritark.stories.data.story.model.story.StoryApiModel

internal data class StoryQuizScreenContent(
    val story: StoryApiModel? = null,
    val quizState: QuizState? = null,
)
