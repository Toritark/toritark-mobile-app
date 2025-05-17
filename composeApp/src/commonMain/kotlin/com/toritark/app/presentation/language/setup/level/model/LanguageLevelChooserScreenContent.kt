package com.toritark.app.presentation.language.setup.level.model

import com.toritark.app.presentation.language.setup.base.model.BaseLanguageSetupScreenContent

internal data class LanguageLevelChooserScreenContent(
    val levels: List<LanguageLevelUiModel> = emptyList(),
    override val isNextButtonEnabled: Boolean = false,
) : BaseLanguageSetupScreenContent
