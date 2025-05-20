package com.toritark.app.presentation.language.setup.level.model

import com.toritark.app.presentation.language.setup.base.model.BaseLanguageSetupScreenState

internal data class LanguageLevelChooserScreenState(
    val levels: List<LanguageLevelUiModel> = emptyList(),
    val selectedLevel: LanguageLevelUiModel? = null,
    override val isNextButtonEnabled: Boolean = false,
) : BaseLanguageSetupScreenState
