package com.toritark.stories.presentation.story.quiz

import co.touchlab.kermit.Logger
import com.toritark.stories.data.story.model.story.StoryApiModel
import com.toritark.stories.data.story.model.story.StoryQuestionAnswerApiModel
import com.toritark.stories.presentation.core_ui.screen.BaseViewModel
import com.toritark.stories.presentation.story.quiz.model.QuizAnswerState
import com.toritark.stories.presentation.story.quiz.model.QuizQuestionState
import com.toritark.stories.presentation.story.quiz.model.QuizState
import com.toritark.stories.util.core.extension.iterable.replaceItemAt
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class StoryQuizViewModel(
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseViewModel(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
) {
    override val logger = Logger.withTag(LOG_TAG)

    private val _story = MutableStateFlow<StoryApiModel?>(null)
    val story = _story.asStateFlow()

    private val _quizState = MutableStateFlow<QuizState?>(null)
    val quizState = _quizState.asStateFlow()

    fun setStory(story: StoryApiModel) {
        logger.d { "setStory: story=$story" }

        if (_story.value == story) return

        _story.value = story
        _quizState.value = QuizState(
            questions = story.questions,
            questionsStates = List(story.questions.size) { QuizQuestionState.NONE },
            currentQuestionIndex = -1,
            answersStates = emptyList(),
            isLastQuestion = false,
            correctAnswers = 0,
            wrongAnswers = 0,
        )
        showNextQuestion()
    }

    fun onCloseClick() {
        logger.d { "onCloseClick" }

        onPopBackStack()
    }

    fun onAnswerSelected(answer: StoryQuestionAnswerApiModel) {
        logger.d { "onAnswerSelected: answer=$answer" }

        val state = quizState.value ?: return

        val currentQuestion = state.questions[state.currentQuestionIndex]
        val currentAnswerIndex = currentQuestion.answers.indexOf(answer)

        if (answer.isCorrect) {
            _quizState.value = state.copy(
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

            _quizState.value = state.copy(
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
    }

    fun onNextClick() {
        logger.d { "onNextClick" }

        if (quizState.value?.isLastQuestion == false) {
            showNextQuestion()
        } else {
            // TODO: Show results
            onPopBackStack()
        }
    }

    private fun showNextQuestion() {
        val state = quizState.value ?: return

        val currentQuestionIndex = state.currentQuestionIndex + 1

        if (currentQuestionIndex >= state.questions.size) {
            logger.w { "showNextQuestion: currentQuestionIndex=$currentQuestionIndex >= questions.size" }
            return
        }

        val currentQuestion = state.questions[currentQuestionIndex]

        _quizState.value = state.copy(
            questionsStates = state.questionsStates.replaceItemAt(currentQuestionIndex, QuizQuestionState.CURRENT),
            currentQuestionIndex = currentQuestionIndex,
            answersStates = List(currentQuestion.answers.size) { QuizAnswerState.NONE },
            isLastQuestion = currentQuestionIndex == state.questions.size - 1,
        )
    }

    private companion object {
        private const val LOG_TAG = "QuizViewModel"
    }
}