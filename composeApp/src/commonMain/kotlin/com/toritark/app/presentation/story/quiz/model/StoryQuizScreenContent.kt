package com.toritark.app.presentation.story.quiz.model

import com.toritark.app.data.story.model.story.story.StoryApiModel

internal data class StoryQuizScreenContent(
    val story: StoryApiModel? = null,
    val quizState: QuizState? = null,
)
