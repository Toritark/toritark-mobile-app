package com.toritark.stories.presentation.story.detail

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.toritark.stories.data.story.model.story.StoryApiModel
import com.toritark.stories.data.story.model.story_request.StoryRequestApiModel
import com.toritark.stories.data.story.model.topic.StoryTopic
import com.toritark.stories.domain.story.interactor.StoriesInteractor
import com.toritark.stories.presentation.core_ui.screen.BaseViewModel
import com.toritark.stories.presentation.story.model.StoryTopicUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import toritark.composeapp.generated.resources.*

internal class StoryDetailViewModel(
    private val storiesInteractor: StoriesInteractor,
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseViewModel(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
) {
    override val logger = Logger.withTag(LOG_TAG)

    private val _storyTopics = MutableStateFlow<List<StoryTopicUiModel>>(emptyList())
    val storyTopics = _storyTopics.asStateFlow()

    private val _selectedStoryTopic = MutableStateFlow<StoryTopicUiModel?>(null)
    val selectedStoryTopic = _selectedStoryTopic.asStateFlow()

    private val _prompt = MutableStateFlow("")
    val prompt = _prompt.asStateFlow()

    private val _isPromptVisible = MutableStateFlow(false)
    val isPromptVisible = _isPromptVisible.asStateFlow()

    private val _isGenerateButtonEnabled = MutableStateFlow(false)
    val isGenerateButtonEnabled = _isGenerateButtonEnabled.asStateFlow()

    private val _story = MutableStateFlow<StoryApiModel?>(null)
    val story = _story.asStateFlow()

    init {
        initialize()
    }

    private fun initialize() {
        initializeStoryTopics()
    }

    private fun initializeStoryTopics() {
        _storyTopics.value = defaultStoryTopics
        _selectedStoryTopic.value = defaultStoryTopics.first()
        onPromptChange(selectedStoryTopic.value?.storyTopic?.prompt ?: "")
    }

    fun onStoryTopicSelected(storyTopic: StoryTopicUiModel) {
        logger.d { "onStoryTopicSelected: storyTopic=${storyTopic.storyTopic}" }

        val previousStoryTopic = _selectedStoryTopic.value

        _selectedStoryTopic.value = storyTopic
        onPromptChange(storyTopic.storyTopic.prompt)

        if (storyTopic.storyTopic is StoryTopic.Custom) {
            _isPromptVisible.value = true
        } else {
            if (_isPromptVisible.value && previousStoryTopic?.storyTopic is StoryTopic.Custom) {
                _isPromptVisible.value = false
            }
        }
    }

    fun onPromptChange(prompt: String) {
        logger.d { "onPromptChange: newPrompt=$prompt" }

        _prompt.value = prompt
        _isGenerateButtonEnabled.value = prompt.isNotBlank()
    }

    fun togglePromptVisibility() {
        logger.d { "togglePromptVisibility: current=${_isPromptVisible.value}" }

        _isPromptVisible.value = !_isPromptVisible.value
    }

    fun onGenerateStoryClick() {
        logger.d { "onGenerateStoryClick" }

        val prompt = prompt.value.takeIf { it.isNotBlank() } ?: return

        setLoadingScreenState()

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

        _story.value = storyRequest.story

        setContentScreenState()
    }

    private fun onStoryGenerationError(storyRequest: StoryRequestApiModel) {
        logger.w { "onStoryGenerationError: storyRequest=$storyRequest" }

        setContentScreenState()
    }

    private companion object {
        private const val LOG_TAG = "StoryDetailViewModel"

        private const val WATCH_STORY_CHECK_INTERVAL_MS = 100L

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
