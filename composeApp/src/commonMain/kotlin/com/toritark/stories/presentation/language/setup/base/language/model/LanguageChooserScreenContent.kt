package com.toritark.stories.presentation.language.setup.base.language.model

import com.toritark.stories.data.language.model.Language
import com.toritark.stories.presentation.language.setup.base.model.BaseLanguageSetupScreenContent

internal data class LanguageChooserScreenContent(
    val languages: List<Language> = emptyList(),
    val preSelectedLanguage: Language? = null,
    override val isNextButtonEnabled: Boolean = false,
) : BaseLanguageSetupScreenContent
