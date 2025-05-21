package com.toritark.app.domain.story.interactor

import co.touchlab.kermit.Logger
import com.toritark.app.data.language.repository.LanguagesRepository
import com.toritark.app.data.story.model.retelling.request.StoryRetellingReviewRequestApiModel
import com.toritark.app.data.story.model.story.request.StoryRequestApiModel
import com.toritark.app.data.story.repository.StoryRequestsApiRepository
import com.toritark.app.domain.story.exception.CreateStoryException
import com.toritark.app.util.core.extension.flow.errorFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

internal interface StoriesInteractor {
    fun createStory(prompt: String): Flow<StoryRequestApiModel>
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

    override fun createStory(prompt: String): Flow<StoryRequestApiModel> {
        logger.d { "createStory: prompt='$prompt'" }

        val learningLanguage = languagesRepository.learningLanguage.value
        val nativeLanguage = languagesRepository.nativeLanguage.value
        val languageLevel = languagesRepository.languageLevel.value

        if (learningLanguage == null || nativeLanguage == null || languageLevel == null) {
            logger.w { "createStory: learningLanguage or nativeLanguage or languageLevel is null" }

            return errorFlow(CreateStoryException.CreateStoryConfigurationException())
        }

        val finalPrompt = getPrompt(rawPrompt = prompt)

        logger.d { "createStory: finalPrompt='$finalPrompt'" }

        return storyRequestsApiRepository
            .createStoryRequest(
                learningLanguage = learningLanguage,
                nativeLanguage = nativeLanguage,
                languageLevel = languageLevel,
                prompt = finalPrompt,
            )
            .flowOn(ioDispatcher)
    }

    private fun getPrompt(rawPrompt: String): String {
        var result = rawPrompt
        val replacements = getRandomPromptPlaceholders()
        replacements.forEach { (placeholder, replacement) ->
            result = result.replace(placeholder, replacement)
        }

        return result
    }

    private fun getRandomPromptPlaceholders(): Collection<PromptPlaceholderReplacement> {
        return listOf(
            PromptPlaceholderReplacement(
                placeholder = PROMPT_PLACEHOLDER_AGE_GROUP,
                replacement = ageGroups.random(),
            ),
            PromptPlaceholderReplacement(
                placeholder = PROMPT_PLACEHOLDER_GENDER,
                replacement = genders.random(),
            ),
            PromptPlaceholderReplacement(
                placeholder = PROMPT_PLACEHOLDER_SETTLEMENT,
                replacement = settlements.random(),
            ),
            PromptPlaceholderReplacement(
                placeholder = PROMPT_PLACEHOLDER_OCCUPATION,
                replacement = occupations.random(),
            ),
            PromptPlaceholderReplacement(
                placeholder = PROMPT_PLACEHOLDER_TRANSPORT_1,
                replacement = transports.random(),
            ),
            PromptPlaceholderReplacement(
                placeholder = PROMPT_PLACEHOLDER_TRANSPORT_2,
                replacement = transports.random(),
            )
        )
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

    data class PromptPlaceholderReplacement(
        val placeholder: String,
        val replacement: String,
    )

    private companion object {
        private const val LOG_TAG = "StoriesInteractor"

        private const val PROMPT_PLACEHOLDER_AGE_GROUP = "{AGE_GROUP}"
        private const val PROMPT_PLACEHOLDER_GENDER = "{GENDER}"
        private const val PROMPT_PLACEHOLDER_SETTLEMENT = "{SETTLEMENT}"
        private const val PROMPT_PLACEHOLDER_OCCUPATION = "{OCCUPATION}"
        private const val PROMPT_PLACEHOLDER_TRANSPORT_1 = "{TRANSPORT_1}"
        private const val PROMPT_PLACEHOLDER_TRANSPORT_2 = "{TRANSPORT_2}"

        val ageGroups = setOf(
            "child",
            "teenager",
            "young adult",
            "middle-aged",
            "elderly",
        )

        val genders = setOf(
            "male",
            "female",
        )

        val occupations = setOf(
            "student",
            "artist",
            "scientist",
            "teacher",
            "farmer",
            "driver",
            "manager",
            "nurse",
            "engineer",
            "chef",
            "musician",
            "writer",
            "electrician",
            "police officer",
            "salesperson",
            "pilot",
            "doctor",
            "architect",
            "entrepreneur",
            "journalist",
            "pharmacist",
            "veterinarian",
            "designer",
            "firefighter",
            "mechanic",
            "barista",
            "construction worker",
            "postman",
            "courier",
        )

        val settlements = setOf(
            "big city",
            "average city",
            "small town",
            "suburb",
            "village",
            "seaside town",
            "island",
            "port city",
        )

        val transports = setOf(
            "car",
            "bike",
            "train",
            "subway",
            "bus",
            "tram",
            "foot",
            "taxi",
            "electric scooter",
        )

    }
}