package com.toritark.stories.data.language.repository

import co.touchlab.kermit.Logger
import com.russhwolf.settings.Settings
import com.toritark.stories.data.language.model.Language
import com.toritark.stories.data.language.model.LanguageLevel
import com.toritark.stories.data.language.model.allLanguages
import com.toritark.stories.data.language.resources.LanguagesApiResources
import com.toritark.stories.util.core.extension.flow.typedFlow
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

interface LanguagesRepository {
    val areAllParametersSet: StateFlow<Boolean?>

    val learningLanguage: StateFlow<Language?>
    val nativeLanguage: StateFlow<Language?>
    val languageLevel: StateFlow<LanguageLevel?>

    fun getLanguages(): Flow<List<Language>>

    suspend fun setLearningLanguage(language: Language)
    suspend fun setNativeLanguage(language: Language)
    suspend fun setLanguageLevel(level: LanguageLevel)
}

internal class LanguagesRepositoryImpl(
    private val httpClient: HttpClient,
    private val settings: Settings,
    private val ioDispatcher: CoroutineDispatcher,
) : LanguagesRepository {

    private val logger = Logger.withTag(LOG_TAG)

    private var cachedLanguages: List<Language>? = null

    private val _areAllParametersSet = MutableStateFlow<Boolean?>(null)
    override val areAllParametersSet = _areAllParametersSet.asStateFlow()

    private val _learningLanguage = MutableStateFlow<Language?>(null)
    override val learningLanguage = _learningLanguage.asStateFlow()

    private val _nativeLanguage = MutableStateFlow<Language?>(null)
    override val nativeLanguage = _nativeLanguage.asStateFlow()

    private val _languageLevel = MutableStateFlow<LanguageLevel?>(null)
    override val languageLevel = _languageLevel.asStateFlow()

    init {
        initializeLanguages()
    }

    private fun initializeLanguages() {
        CoroutineScope(ioDispatcher + SupervisorJob()).launch {
            getLanguages()
                .flowOn(ioDispatcher)
                .collect { languages ->
                    initializeLanguages(languages = languages)
                }
        }
    }

    private fun initializeLanguages(languages: List<Language>) {
        logger.d { "initializeLanguages: languages=${languages.size}" }

        _learningLanguage.value = getLanguage(key = LEARNING_LANGUAGE_KEY, languages = languages)
        _nativeLanguage.value = getLanguage(key = NATIVE_LANGUAGE_KEY, languages = languages)
        _languageLevel.value = settings.getStringOrNull(LANGUAGE_LEVEL_KEY)?.let(LanguageLevel::fromValue)

        updateAreAllParametersSet()
    }

    private fun getLanguage(key: String, languages: List<Language>): Language? {
        return languages.find { it.isoCode == settings.getStringOrNull(key) }
    }

    override suspend fun setLearningLanguage(language: Language) {
        withContext(ioDispatcher) {
            settings.putString(LEARNING_LANGUAGE_KEY, language.isoCode)

            _learningLanguage.value = language
            updateAreAllParametersSet()
        }
    }

    override suspend fun setNativeLanguage(language: Language) {
        withContext(ioDispatcher) {
            settings.putString(NATIVE_LANGUAGE_KEY, language.isoCode)

            _nativeLanguage.value = language
            updateAreAllParametersSet()
        }
    }

    override suspend fun setLanguageLevel(level: LanguageLevel) {
        withContext(ioDispatcher) {
            settings.putString(LANGUAGE_LEVEL_KEY, level.value)

            _languageLevel.value = level
            updateAreAllParametersSet()
        }
    }

    private fun updateAreAllParametersSet() {
        logger.d { "updateAreAllParametersSet" }

        _areAllParametersSet.value = _learningLanguage.value != null &&
                _nativeLanguage.value != null &&
                _languageLevel.value != null
    }

    override fun getLanguages(): Flow<List<Language>> {
        cachedLanguages?.let { return flowOf(it) }

        logger.d { "getLanguages: no cached languages, fetching from the backend" }

        return typedFlow {
            httpClient
                .get(LanguagesApiResources.List())
                .body<List<Language>>()
                .also { cachedLanguages = it }
        }
            .catch { t ->
                logger.w(t) { "getLanguages: failed, ${t.message}" }

                emit(allLanguages)
            }
            .flowOn(ioDispatcher)
    }

    private companion object {
        private const val LOG_TAG = "LanguagesRepository"

        private const val LEARNING_LANGUAGE_KEY = "learning_language"
        private const val NATIVE_LANGUAGE_KEY = "native_language"
        private const val LANGUAGE_LEVEL_KEY = "language_level"
    }
}