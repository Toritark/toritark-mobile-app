package com.toritark.stories.presentation.story.quiz.model

import com.toritark.stories.data.story.model.story.story.StoryQuestionApiModel

internal data class QuizState(
    val questions: List<StoryQuestionApiModel>,
    val questionsStates: List<QuizQuestionState>,
    val currentQuestionIndex: Int,
    val answersStates: List<QuizAnswerState>,
    val isLastQuestion: Boolean,
    val correctAnswers: Int,
    val wrongAnswers: Int,
)
