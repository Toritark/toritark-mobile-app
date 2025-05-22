package com.toritark.app.data.story.repository

import com.toritark.app.data.language.model.Language
import com.toritark.app.data.language.model.LanguageLevel
import com.toritark.app.data.story.exception.QuotaExceededException
import com.toritark.app.data.story.model.retelling.request.CreateStoryRetellingReviewRequestApiModel
import com.toritark.app.data.story.model.retelling.request.StoryRetellingReviewRequestApiModel
import com.toritark.app.data.story.model.story.request.CreateStoryRequestApiModel
import com.toritark.app.data.story.model.story.request.StoryRequestApiModel
import com.toritark.app.data.story.model.story.story.StoryTopicApiModel
import com.toritark.app.data.story.resource.StoryApiResources
import com.toritark.app.util.core.extension.flow.typedFlow
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

internal interface StoryRequestsApiRepository {

    fun createStoryRequest(
        learningLanguage: Language,
        nativeLanguage: Language,
        languageLevel: LanguageLevel,
        topic: StoryTopicApiModel,
        prompt: String?,
    ): Flow<StoryRequestApiModel>

    fun getStoryRequest(id: Long): Flow<StoryRequestApiModel>

    fun createStoryRetellingReviewRequest(
        storyRequestId: Long,
        retelling: String,
    ): Flow<StoryRetellingReviewRequestApiModel>

    fun getStoryRetellingReviewRequest(id: Long): Flow<StoryRetellingReviewRequestApiModel>
}

internal class StoryRequestsApiRepositoryImpl(
    private val httpClient: HttpClient,
    private val ioDispatcher: CoroutineDispatcher,
) : StoryRequestsApiRepository {

    override fun createStoryRequest(
        learningLanguage: Language,
        nativeLanguage: Language,
        languageLevel: LanguageLevel,
        topic: StoryTopicApiModel,
        prompt: String?,
    ): Flow<StoryRequestApiModel> {
        val request = CreateStoryRequestApiModel(
            learningLanguageCode = learningLanguage.isoCode,
            nativeLanguageCode = nativeLanguage.isoCode,
            languageLevel = languageLevel,
            topic = topic,
            prompt = prompt,
        )

        return typedFlow {
            httpClient.post(StoryApiResources.StoryRequests.Create()) {
                setBody(request)
                expectSuccess = true
            }.body<StoryRequestApiModel>()
        }
            .catch { t ->
                if (t is ClientRequestException && t.response.status == HttpStatusCode.TooManyRequests) {
                    throw QuotaExceededException()
                }

                throw t
            }
            .flowOn(ioDispatcher)
    }

    override fun getStoryRequest(id: Long): Flow<StoryRequestApiModel> {
        return typedFlow {
            httpClient.get(StoryApiResources.StoryRequests.Get(id = id)) {
                expectSuccess = true

            }.body<StoryRequestApiModel>()
        }
    }

    override fun createStoryRetellingReviewRequest(
        storyRequestId: Long,
        retelling: String,
    ): Flow<StoryRetellingReviewRequestApiModel> {
        val request = CreateStoryRetellingReviewRequestApiModel(
            storyRequestId = storyRequestId,
            retelling = retelling,
        )

        return typedFlow {
            httpClient.post(StoryApiResources.StoryRetellingsReviews.Create()) {
                setBody(request)
                expectSuccess = true
            }.body<StoryRetellingReviewRequestApiModel>()
        }
            .catch { t ->
                if (t is ClientRequestException && t.response.status == HttpStatusCode.TooManyRequests) {
                    throw QuotaExceededException()
                }

                throw t
            }
            .flowOn(ioDispatcher)
    }

    override fun getStoryRetellingReviewRequest(id: Long): Flow<StoryRetellingReviewRequestApiModel> {
        return typedFlow {
            httpClient.get(StoryApiResources.StoryRetellingsReviews.Get(id = id)) {
                expectSuccess = true
            }.body<StoryRetellingReviewRequestApiModel>()
        }
    }
}