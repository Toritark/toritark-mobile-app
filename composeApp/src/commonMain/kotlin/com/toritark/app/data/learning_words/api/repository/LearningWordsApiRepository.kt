package com.toritark.app.data.learning_words.api.repository

import co.touchlab.kermit.Logger
import com.toritark.app.data.core_api.base.repository.ApiRepository
import com.toritark.app.data.core_api.base.repository.BaseApiRepository
import com.toritark.app.data.learning_words.api.model.LearningStatsApiModel
import com.toritark.app.data.learning_words.api.model.SentenceToLearnApiModel
import com.toritark.app.data.learning_words.api.model.request.AddWordsToLearnRequest
import com.toritark.app.data.learning_words.api.model.request.SentenceToLearnCreateApiModel
import com.toritark.app.data.learning_words.api.model.request.UpdateSentenceResultsRequest
import com.toritark.app.data.learning_words.api.resource.LearningWordsApiResources
import com.toritark.app.data.learning_words.model.SentenceToLearn
import com.toritark.app.util.core.extension.flow.typedFlow
import com.toritark.app.util.core.extension.flow.unitFlow
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

internal interface LearningWordsApiRepository : ApiRepository {
    fun addWords(
        learningLanguageCode: String,
        nativeLanguageCode: String,
        words: Set<String>,
        sentences: Set<SentenceToLearn>,
    ): Flow<Unit>

    fun getNextSentenceToLearn(
        learningLanguageCode: String,
        nativeLanguageCode: String,
    ): Flow<SentenceToLearnApiModel>

    fun getLearningStats(
        learningLanguageCode: String,
        nativeLanguageCode: String,
    ): Flow<LearningStatsApiModel>

    fun updateSentenceResults(
        sentenceId: Long,
        correctWordsIds: Set<Long>,
        incorrectWordsIds: Set<Long>,
    ): Flow<Unit>

    fun setSentenceLearned(sentenceId: Long): Flow<Unit>
}

internal class LearningWordsApiRepositoryImpl(
    httpClient: HttpClient,
    ioDispatcher: CoroutineDispatcher,
    defaultDispatcher: CoroutineDispatcher,
) : BaseApiRepository(
    httpClient = httpClient,
    ioDispatcher = ioDispatcher,
    defaultDispatcher = defaultDispatcher,
), LearningWordsApiRepository {

    private val logger = Logger.withTag(LOG_TAG)

    override fun addWords(
        learningLanguageCode: String,
        nativeLanguageCode: String,
        words: Set<String>,
        sentences: Set<SentenceToLearn>,
    ): Flow<Unit> {
        return unitFlow {
            val request = AddWordsToLearnRequest(
                learningLanguageCode = learningLanguageCode,
                nativeLanguageCode = nativeLanguageCode,
                words = words,
                sentences = sentences.map { sentence ->
                    SentenceToLearnCreateApiModel(
                        learningLanguageText = sentence.learningLanguageText,
                        nativeLanguageText = sentence.nativeLanguageText,
                    )
                },
            )

            httpClient.post(resource = LearningWordsApiResources.Sentences.Add()) {
                setBody(request)
                expectSuccess = true
            }
        }.flowOn(ioDispatcher)
    }

    override fun getNextSentenceToLearn(
        learningLanguageCode: String,
        nativeLanguageCode: String,
    ): Flow<SentenceToLearnApiModel> {
        return typedFlow<SentenceToLearnApiModel> {
            httpClient.get(
                resource = LearningWordsApiResources.Sentences.Next(
                    learningLanguageCode = learningLanguageCode,
                    nativeLanguageCode = nativeLanguageCode,
                )
            ) {
                expectSuccess = true
            }.body()
        }.flowOn(ioDispatcher)
            .catch { t ->
                if (t !is ClientRequestException || t.response.status != HttpStatusCode.NotFound) {
                    throw t
                }

                // 404 == no sentences to learn - returning empty flow
            }
    }

    override fun getLearningStats(
        learningLanguageCode: String,
        nativeLanguageCode: String,
    ): Flow<LearningStatsApiModel> {
        return typedFlow<LearningStatsApiModel> {
            httpClient.get(
                resource = LearningWordsApiResources.Stats(
                    learningLanguageCode = learningLanguageCode,
                    nativeLanguageCode = nativeLanguageCode,
                )
            ) {
                expectSuccess = true
            }.body()
        }.flowOn(ioDispatcher)
    }

    override fun updateSentenceResults(
        sentenceId: Long,
        correctWordsIds: Set<Long>,
        incorrectWordsIds: Set<Long>,
    ): Flow<Unit> {
        return unitFlow {
            val request = UpdateSentenceResultsRequest(
                correctWordsIds = correctWordsIds,
                incorrectWordsIds = incorrectWordsIds,
            )

            httpClient.post(
                resource = LearningWordsApiResources.Sentences.Sentence.Results.Update(
                    parent = LearningWordsApiResources.Sentences.Sentence.Results(
                        parent = LearningWordsApiResources.Sentences.Sentence(id = sentenceId)
                    ),
                )
            ) {
                setBody(request)
                expectSuccess = true
            }
        }.flowOn(ioDispatcher)
    }

    override fun setSentenceLearned(sentenceId: Long): Flow<Unit> {
        return unitFlow {
            httpClient.post(
                resource = LearningWordsApiResources.Sentences.Sentence.Learned(
                    parent = LearningWordsApiResources.Sentences.Sentence(id = sentenceId)
                )
            ) {
                expectSuccess = true
            }
        }.flowOn(ioDispatcher)
    }

    private companion object {
        private const val LOG_TAG = "LearningWordsApiRepository"
    }
}