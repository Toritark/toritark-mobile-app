package com.toritark.stories.domain.story.interactor

import co.touchlab.kermit.Logger
import com.toritark.stories.data.language.repository.LanguagesRepository
import com.toritark.stories.data.story.model.story_request.StoryRequestApiModel
import com.toritark.stories.data.story.repository.StoryRequestsApiRepository
import com.toritark.stories.domain.story.exception.CreateStoryException
import com.toritark.stories.util.core.extension.flow.errorFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

internal interface StoriesInteractor {
    fun createStory(prompt: String): Flow<StoryRequestApiModel>
    fun getStory(requestId: Long): Flow<StoryRequestApiModel>
}

internal class StoriesInteractorImpl(
    private val storyRequestsApiRepository: StoryRequestsApiRepository,
    private val languagesRepository: LanguagesRepository,
    private val defaultDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher,
) : StoriesInteractor {

    private val logger = Logger.withTag(LOG_TAG)

    override fun createStory(prompt: String): Flow<StoryRequestApiModel> {
        logger.d { "createStory: prompt='$prompt'" }

        val learningLanguage = languagesRepository.learningLanguage.value
        val nativeLanguage = languagesRepository.nativeLanguage.value
        val languageLevel = languagesRepository.languageLevel.value

        if (learningLanguage == null || nativeLanguage == null || languageLevel == null) {
            logger.w { "createStory: learningLanguage or nativeLanguage or languageLevel is null" }

            return errorFlow(CreateStoryException.CreateStoryConfigurationException())
        }

        return storyRequestsApiRepository
            .createStoryRequest(
                learningLanguage = learningLanguage,
                nativeLanguage = nativeLanguage,
                languageLevel = languageLevel,
                prompt = prompt,
            )
            .flowOn(ioDispatcher)
    }

    override fun getStory(requestId: Long): Flow<StoryRequestApiModel> {
        logger.d { "getStory: requestId='$requestId'" }

        return storyRequestsApiRepository
            .getStoryRequest(id = requestId)
            .flowOn(ioDispatcher)
    }

    private companion object {
        private const val LOG_TAG = "StoriesInteractor"
    }
}