package com.toritark.app.presentation.language.setup.base.language.model

import com.toritark.app.presentation.language.model.LanguageUiModel
import com.toritark.app.presentation.language.setup.base.model.BaseLanguageSetupScreenState

internal data class LanguageChooserScreenState(
    val languages: List<LanguageUiModel> = emptyList(),
    val preSelectedLanguage: LanguageUiModel? = null,
    override val isNextButtonEnabled: Boolean = false,
) : BaseLanguageSetupScreenState
