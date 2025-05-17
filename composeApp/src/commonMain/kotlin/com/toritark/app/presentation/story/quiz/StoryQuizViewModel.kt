package com.toritark.app.presentation.story.quiz

import co.touchlab.kermit.Logger
import com.toritark.app.data.story.model.story.story.StoryApiModel
import com.toritark.app.data.story.model.story.story.StoryQuestionAnswerApiModel
import com.toritark.app.presentation.core_ui.screen.BaseViewModel
import com.toritark.app.presentation.story.quiz.model.QuizAnswerState
import com.toritark.app.presentation.story.quiz.model.QuizQuestionState
import com.toritark.app.presentation.story.quiz.model.QuizState
import com.toritark.app.presentation.story.quiz.model.StoryQuizScreenContent
import com.toritark.app.util.core.extension.iterable.replaceItemAt
import kotlinx.coroutines.CoroutineDispatcher

internal class StoryQuizViewModel(
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseViewModel<StoryQuizScreenContent>(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
    defaultContentValue = StoryQuizScreenContent(),
) {
    override val logger = Logger.withTag(LOG_TAG)

    fun setStory(story: StoryApiModel) {
        logger.d { "setStory: story=$story" }

        if (contentValue.story == story) return

        updateAndShowContent {
            copy(
                story = story,
                quizState = QuizState(
                    questions = story.questions,
                    questionsStates = List(story.questions.size) { QuizQuestionState.NONE },
                    currentQuestionIndex = -1,
                    answersStates = emptyList(),
                    isLastQuestion = false,
                    correctAnswers = 0,
                    wrongAnswers = 0,
                )
            )
        }

        showNextQuestion()
    }

    fun onCloseClick() {
        logger.d { "onCloseClick" }

        onPopBackStack()
    }

    fun onAnswerSelected(answer: StoryQuestionAnswerApiModel) {
        logger.d { "onAnswerSelected: answer=$answer" }

        val state = contentValue.quizState ?: return

        val currentQuestion = state.questions[state.currentQuestionIndex]
        val currentAnswerIndex = currentQuestion.answers.indexOf(answer)

        val quizState = if (answer.isCorrect) {
            state.copy(
                questionsStates = state.questionsStates.replaceItemAt(
                    state.currentQuestionIndex,
                    QuizQuestionState.CORRECT
                ),
                answersStates = state.answersStates.mapIndexed { index, answerState ->
                    when (index) {
                        currentAnswerIndex -> QuizAnswerState.CORRECT
                        else -> answerState
                    }
                },
                correctAnswers = state.correctAnswers + 1,
            )
        } else {
            val correctAnswerIndex = currentQuestion.answers.indexOfFirst { it.isCorrect }

            state.copy(
                questionsStates = state.questionsStates.replaceItemAt(
                    state.currentQuestionIndex,
                    QuizQuestionState.WRONG
                ),
                answersStates = state.answersStates.mapIndexed { index, answerState ->
                    when (index) {
                        correctAnswerIndex -> QuizAnswerState.CORRECT
                        currentAnswerIndex -> QuizAnswerState.WRONG
                        else -> answerState
                    }
                },
                wrongAnswers = state.wrongAnswers + 1,
            )
        }

        updateAndShowContent {
            copy(quizState = quizState)
        }
    }

    fun onNextClick() {
        logger.d { "onNextClick" }

        if (contentValue.quizState?.isLastQuestion == false) {
            showNextQuestion()
        } else {
            // TODO: Show results
            onPopBackStack()
        }
    }

    private fun showNextQuestion() {
        val state = contentValue.quizState ?: return

        val currentQuestionIndex = state.currentQuestionIndex + 1

        if (currentQuestionIndex >= state.questions.size) {
            logger.w { "showNextQuestion: currentQuestionIndex=$currentQuestionIndex >= questions.size" }
            return
        }

        val currentQuestion = state.questions[currentQuestionIndex]

        updateAndShowContent {
            copy(
                quizState = state.copy(
                    questionsStates = state.questionsStates.replaceItemAt(
                        currentQuestionIndex,
                        QuizQuestionState.CURRENT
                    ),
                    currentQuestionIndex = currentQuestionIndex,
                    answersStates = List(currentQuestion.answers.size) { QuizAnswerState.NONE },
                    isLastQuestion = currentQuestionIndex == state.questions.size - 1,
                )
            )
        }
    }

    private companion object {
        private const val LOG_TAG = "QuizViewModel"
    }
}