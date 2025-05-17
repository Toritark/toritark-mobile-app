package com.toritark.app.presentation.story.detail

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.toritark.app.data.story.model.story.request.StoryRequestApiModel
import com.toritark.app.data.story.model.story.story.StoryApiModel
import com.toritark.app.data.story.model.topic.StoryTopic
import com.toritark.app.domain.story.interactor.StoriesInteractor
import com.toritark.app.presentation.core_ui.screen.BaseViewModel
import com.toritark.app.presentation.story.detail.model.StoryDetailScreenContent
import com.toritark.app.presentation.story.model.StoryTopicUiModel
import com.toritark.app.presentation.story.nav.StoryNavDestination
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import toritark.composeapp.generated.resources.*

internal class StoryDetailViewModel(
    private val storiesInteractor: StoriesInteractor,
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseViewModel<StoryDetailScreenContent>(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
    defaultContentValue = StoryDetailScreenContent(),
) {
    override val logger = Logger.withTag(LOG_TAG)

    init {
        initialize()
    }

    private fun initialize() {
        initializeStoryTopics()
    }

    private fun initializeStoryTopics() {
        updateAndShowContent {
            copy(
                topics = defaultStoryTopics,
                selectedTopic = defaultStoryTopics.first(),
            )
        }
        onPromptChange(contentValue.selectedTopic?.storyTopic?.prompt ?: "")
    }

    fun onStoryTopicSelected(storyTopic: StoryTopicUiModel) {
        logger.d { "onStoryTopicSelected: storyTopic=${storyTopic.storyTopic}" }

        val previousStoryTopic = contentValue.selectedTopic

        onPromptChange(storyTopic.storyTopic.prompt)

        updateAndShowContent {
            copy(
                selectedTopic = storyTopic,
                isPromptInputVisible = when {
                    storyTopic.storyTopic is StoryTopic.Custom -> true
                    contentValue.isPromptInputVisible && previousStoryTopic?.storyTopic is StoryTopic.Custom -> false
                    else -> contentValue.isPromptInputVisible
                }
            )
        }
    }

    fun onPromptChange(prompt: String) {
        logger.d { "onPromptChange: newPrompt=$prompt" }

        updateAndShowContent {
            copy(
                promptText = prompt,
                isGenerateButtonEnabled = isGeneratedButtonEnabled(promptText = prompt),
            )
        }
    }

    private fun isGeneratedButtonEnabled(
        promptText: String = contentValue.promptText,
        storyState: StoryDetailScreenContent.StoryState = contentValue.storyState,
    ): Boolean {
        return promptText.isNotBlank() && storyState !is StoryDetailScreenContent.StoryState.Creating
    }

    fun togglePromptVisibility() {
        logger.d { "togglePromptVisibility: current=${contentValue.isPromptInputVisible}" }

        updateAndShowContent {
            copy(isPromptInputVisible = !isPromptInputVisible)
        }
    }

    fun onGenerateStoryClick() {
        logger.d { "onGenerateStoryClick" }

        val prompt = contentValue.promptText.takeIf { it.isNotBlank() } ?: return

        updateAndShowContent {
            copy(
                storyState = StoryDetailScreenContent.StoryState.Creating,
                isGenerateButtonEnabled = isGeneratedButtonEnabled(storyState = StoryDetailScreenContent.StoryState.Creating),
            )
        }

        viewModelScope.launch {
            storiesInteractor
                .createStory(
                    prompt = prompt,
                )
                .onErrorShowMessage() // FIXME: Not displayed currently!
                // FIXME: Handle quota exceeded
                .collect { storyRequest ->
                    logger.d { "onGenerateStoryClick: storyRequest=$storyRequest" }

                    startWatchingStory(storyId = storyRequest.id)
                }
        }
    }

    private fun startWatchingStory(storyId: Long) {
        logger.d { "startWatchingStory: storyId=$storyId" }

        viewModelScope.launch(defaultDispatcher) {
            var isGenerating = true

            while (isGenerating) {
                delay(WATCH_STORY_CHECK_INTERVAL_MS)

                storiesInteractor
                    .getStory(storyId)
                    .catch { t ->
                        logger.w(t) { "startWatchingStory: error=${t.message}" }
                    }
                    .collect { story ->
                        when (story.status) {
                            StoryRequestApiModel.Status.PENDING,
                            StoryRequestApiModel.Status.CREATING,
                                -> {
                                logger.d { "startWatchingStory: creating" }
                            }

                            StoryRequestApiModel.Status.CREATED -> {
                                isGenerating = false

                                onStoryGenerated(storyRequest = story)
                            }

                            StoryRequestApiModel.Status.FAILED -> {
                                isGenerating = false

                                onStoryGenerationError(storyRequest = story)
                            }
                        }
                    }
            }
        }
    }

    private fun onStoryGenerated(storyRequest: StoryRequestApiModel) {
        logger.d { "onStoryGenerated: storyRequest=$storyRequest" }

        updateAndShowContent {
            val newStoryState = when (storyRequest.story) {
                null -> StoryDetailScreenContent.StoryState.Empty
                else -> StoryDetailScreenContent.StoryState.Created(
                    storyRequestId = storyRequest.id,
                    story = storyRequest.story
                )
            }
            copy(
                storyState = newStoryState,
                isGenerateButtonEnabled = isGeneratedButtonEnabled(storyState = newStoryState),
            )
        }
    }

    private fun onStoryGenerationError(storyRequest: StoryRequestApiModel) {
        logger.w { "onStoryGenerationError: storyRequest=$storyRequest" }

        updateAndShowContent {
            copy(
                storyState = StoryDetailScreenContent.StoryState.Empty,
                isGenerateButtonEnabled = isGeneratedButtonEnabled(storyState = StoryDetailScreenContent.StoryState.Empty),
            )
        }

        // TODO: Show error
    }

    fun onStoryClick() {
        logger.d { "onStoryClick" }

        val story = story ?: return

        onNavigateTo(
            StoryNavDestination.Text(
                story = story,
            )
        ) {}
    }

    fun onStoryQuestionsClick() {
        logger.d { "onStoryQuestionsClick" }

        val story = story ?: return

        onNavigateTo(
            StoryNavDestination.Quiz(
                story = story,
            )
        ) {}
    }

    private val story: StoryApiModel?
        get() {
            return when (val storyState = contentValue.storyState) {
                is StoryDetailScreenContent.StoryState.Created -> storyState.story
                else -> null
            }
        }

    private companion object {
        private const val LOG_TAG = "StoryDetailViewModel"

        private const val WATCH_STORY_CHECK_INTERVAL_MS = 300L

        private val defaultStoryTopics = listOf(
            StoryTopicUiModel(
                storyTopic = StoryTopic.DailyRoutine,
                nameResource = Res.string.title_story_topic_daily_routine,
            ),
            StoryTopicUiModel(
                storyTopic = StoryTopic.StoreDialogue,
                nameResource = Res.string.title_story_topic_store_dialog,
            ),
            StoryTopicUiModel(
                storyTopic = StoryTopic.FavoriteAnimal,
                nameResource = Res.string.title_story_topic_favorite_animal,
            ),
            StoryTopicUiModel(
                storyTopic = StoryTopic.Walk,
                nameResource = Res.string.title_story_topic_walk,
            ),
            StoryTopicUiModel(
                storyTopic = StoryTopic.MeetingNewFriend,
                nameResource = Res.string.title_story_topic_meeting_new_friend,
            ),
            StoryTopicUiModel(
                storyTopic = StoryTopic.SpecialDay,
                nameResource = Res.string.title_story_topic_special_day,
            ),
            StoryTopicUiModel(
                storyTopic = StoryTopic.MyDream,
                nameResource = Res.string.title_story_topic_my_dream,
            ),
            StoryTopicUiModel(
                storyTopic = StoryTopic.MyRoom,
                nameResource = Res.string.title_story_topic_my_room,
            ),
            StoryTopicUiModel(
                storyTopic = StoryTopic.Family,
                nameResource = Res.string.title_story_topic_family,
            ),
            StoryTopicUiModel(
                storyTopic = StoryTopic.Custom,
                nameResource = Res.string.title_story_topic_custom,
            ),
        )
    }
}
