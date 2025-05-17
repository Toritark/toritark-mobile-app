package com.toritark.app.presentation.story.quiz.model

import com.toritark.app.data.story.model.story.story.StoryQuestionApiModel

internal data class QuizState(
    val questions: List<StoryQuestionApiModel>,
    val questionsStates: List<QuizQuestionState>,
    val currentQuestionIndex: Int,
    val answersStates: List<QuizAnswerState>,
    val isLastQuestion: Boolean,
    val correctAnswers: Int,
    val wrongAnswers: Int,
)
