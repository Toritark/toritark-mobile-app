package com.toritark.app.presentation.language.setup.base.language.model

import com.toritark.app.data.language.model.Language
import com.toritark.app.presentation.language.setup.base.model.BaseLanguageSetupScreenContent

internal data class LanguageChooserScreenContent(
    val languages: List<Language> = emptyList(),
    val preSelectedLanguage: Language? = null,
    override val isNextButtonEnabled: Boolean = false,
) : BaseLanguageSetupScreenContent
