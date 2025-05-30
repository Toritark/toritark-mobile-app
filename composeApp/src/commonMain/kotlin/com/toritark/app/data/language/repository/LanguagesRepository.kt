package com.toritark.app.data.language.repository

import co.touchlab.kermit.Logger
import com.russhwolf.settings.Settings
import com.toritark.app.data.language.model.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

interface LanguagesRepository {
    val areAllParametersSet: StateFlow<Boolean?>

    val learningLanguage: StateFlow<Language?>
    val nativeLanguage: StateFlow<Language?>
    val languageLevel: StateFlow<LanguageLevel?>

    val languagesWithLevel: StateFlow<LanguagesWithLevel?>

    fun getAllLanguages(): List<Language>
    fun getLearningLanguages(): List<Language>

    fun getLanguagesWithLevel(): Flow<LanguagesWithLevel>

    suspend fun setLearningLanguage(language: Language)
    suspend fun setNativeLanguage(language: Language)
    suspend fun setLanguageLevel(level: LanguageLevel)
}

internal class LanguagesRepositoryImpl(
    private val settings: Settings,
    private val defaultDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher,
) : LanguagesRepository {

    private val logger = Logger.withTag(LOG_TAG)

    private val coroutineScope = CoroutineScope(defaultDispatcher)

    private val _areAllParametersSet = MutableStateFlow<Boolean?>(null)
    override val areAllParametersSet = _areAllParametersSet.asStateFlow()

    private val _learningLanguage = MutableStateFlow<Language?>(null)
    override val learningLanguage = _learningLanguage.asStateFlow()

    private val _nativeLanguage = MutableStateFlow<Language?>(null)
    override val nativeLanguage = _nativeLanguage.asStateFlow()

    private val _languageLevel = MutableStateFlow<LanguageLevel?>(null)
    override val languageLevel = _languageLevel.asStateFlow()

    override val languagesWithLevel = combine(
        learningLanguage.filterNotNull(),
        nativeLanguage.filterNotNull(),
        languageLevel.filterNotNull(),
    ) { learningLanguage, nativeLanguage, languageLevel ->
        LanguagesWithLevel(
            learningLanguage = learningLanguage,
            nativeLanguage = nativeLanguage,
            languageLevel = languageLevel,
        )
    }.stateIn(coroutineScope, SharingStarted.Lazily, null)

    init {
        initializeLanguages()
    }

    private fun initializeLanguages() {
        logger.d { "initializeLanguages" }

        _learningLanguage.value = getLanguage(key = LEARNING_LANGUAGE_KEY, languages = getLearningLanguages())
        _nativeLanguage.value = getLanguage(key = NATIVE_LANGUAGE_KEY, languages = getAllLanguages())
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

    override fun getAllLanguages(): List<Language> {
        return allLanguages
    }

    override fun getLearningLanguages(): List<Language> {
        return learningLanguages
    }

    override fun getLanguagesWithLevel(): Flow<LanguagesWithLevel> {
        return languagesWithLevel
            .filterNotNull()
            .take(1)
            .flowOn(defaultDispatcher)
    }

    private companion object {
        private const val LOG_TAG = "LanguagesRepository"

        private const val LEARNING_LANGUAGE_KEY = "learning_language"
        private const val NATIVE_LANGUAGE_KEY = "native_language"
        private const val LANGUAGE_LEVEL_KEY = "language_level"
    }
}