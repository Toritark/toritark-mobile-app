package com.toritark.stories.presentation.language.setup.level

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.toritark.stories.data.language.model.LanguageLevel
import com.toritark.stories.data.language.repository.LanguagesRepository
import com.toritark.stories.presentation.language.setup.base.BaseLanguageSetupViewModel
import com.toritark.stories.presentation.language.setup.level.model.LanguageLevelUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import toritark.composeapp.generated.resources.*

internal class LanguageLevelChooserViewModel(
    private val languagesRepository: LanguagesRepository,
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseLanguageSetupViewModel(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
) {
    override val logger = Logger.withTag(LOG_TAG)

    private var languageLevel: LanguageLevel? = null

    private val _languageLevels = MutableStateFlow<List<LanguageLevelUiModel>>(emptyList())
    val languageLevels = _languageLevels.asStateFlow()

    init {
        initialize()
    }

    override fun initialize() {
        _languageLevels.value = listOf(
            LanguageLevelUiModel(
                languageLevel = LanguageLevel.A1,
                titleStringResource = Res.string.title_language_level_none,
                descriptionStringResource = Res.string.desc_language_level_none,
            ),
            LanguageLevelUiModel(
                languageLevel = LanguageLevel.A2,
                titleStringResource = Res.string.title_language_level_a1,
                descriptionStringResource = Res.string.desc_language_level_a1,
            ),
            LanguageLevelUiModel(
                languageLevel = LanguageLevel.B1,
                titleStringResource = Res.string.title_language_level_a2,
                descriptionStringResource = Res.string.desc_language_level_a2,
            ),
            LanguageLevelUiModel(
                languageLevel = LanguageLevel.B2,
                titleStringResource = Res.string.title_language_level_b1,
                descriptionStringResource = Res.string.desc_language_level_b1,
            ),
            LanguageLevelUiModel(
                languageLevel = LanguageLevel.C1,
                titleStringResource = Res.string.title_language_level_c1,
                descriptionStringResource = Res.string.desc_language_level_c1,
            ),
        )
    }

    fun onLanguageLevelSelected(languageLevel: LanguageLevelUiModel) {
        logger.d { "onLanguageLevelSelected: level=${languageLevel.languageLevel}" }

        this.languageLevel = languageLevel.languageLevel
        this._isNextButtonEnabled.value = true
    }

    override fun onNextButtonClick() {
        logger.d { "onNextButtonClick" }

        val languageLevel = languageLevel ?: return

        setLoadingScreenState()

        viewModelScope.launch {
            languagesRepository.setLanguageLevel(languageLevel)
            onPopBackStack()
        }
    }

    private companion object {
        private const val LOG_TAG = "LanguageLevelChooserViewModel"
    }
}