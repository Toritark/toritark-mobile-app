package com.toritark.app.domain.story.interactor

import co.touchlab.kermit.Logger
import com.toritark.app.data.language.repository.LanguagesRepository
import com.toritark.app.data.story.model.retelling.request.StoryRetellingReviewRequestApiModel
import com.toritark.app.data.story.model.story.request.StoryRequestApiModel
import com.toritark.app.data.story.model.story.story.StoryTopicApiModel
import com.toritark.app.data.story.repository.StoryRequestsApiRepository
import com.toritark.app.domain.story.exception.CreateStoryException
import com.toritark.app.util.core.extension.flow.errorFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

internal interface StoriesInteractor {
    fun createStory(topic: StoryTopicApiModel, prompt: String?): Flow<StoryRequestApiModel>
    fun getStory(requestId: Long): Flow<StoryRequestApiModel>

    fun createStoryRetellingReview(
        storyRequestId: Long,
        retelling: String,
    ): Flow<StoryRetellingReviewRequestApiModel>

    fun getStoryRetellingReview(requestId: Long): Flow<StoryRetellingReviewRequestApiModel>
}

internal class StoriesInteractorImpl(
    private val storyRequestsApiRepository: StoryRequestsApiRepository,
    private val languagesRepository: LanguagesRepository,
    private val defaultDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher,
) : StoriesInteractor {

    private val logger = Logger.withTag(LOG_TAG)

    override fun createStory(topic: StoryTopicApiModel, prompt: String?): Flow<StoryRequestApiModel> {
        logger.d { "createStory: topic=$topic prompt='$prompt'" }

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
                topic = topic,
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

    override fun createStoryRetellingReview(
        storyRequestId: Long,
        retelling: String,
    ): Flow<StoryRetellingReviewRequestApiModel> {
        logger.d { "createStoryRetellingReview: storyRequestId=$storyRequestId, retelling='$retelling'" }

        return storyRequestsApiRepository
            .createStoryRetellingReviewRequest(
                storyRequestId = storyRequestId,
                retelling = retelling,
            )
            .flowOn(ioDispatcher)
    }

    override fun getStoryRetellingReview(requestId: Long): Flow<StoryRetellingReviewRequestApiModel> {
        logger.d { "getStoryRetellingReview: requestId=$requestId" }

        return storyRequestsApiRepository
            .getStoryRetellingReviewRequest(id = requestId)
            .flowOn(ioDispatcher)
    }

    private companion object {
        private const val LOG_TAG = "StoriesInteractor"
    }
}