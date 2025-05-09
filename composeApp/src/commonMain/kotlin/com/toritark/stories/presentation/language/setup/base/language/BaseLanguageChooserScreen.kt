package com.toritark.stories.presentation.language.setup.base.language

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.toritark.stories.presentation.language.base.LanguageChooser
import com.toritark.stories.presentation.language.setup.base.BaseLanguageSetupScreen
import org.jetbrains.compose.resources.StringResource

@Composable
internal fun BaseLanguageChooserScreen(
    titleStringResource: StringResource,
    nextButtonStringResource: StringResource,
    viewModel: BaseLanguageChooserViewModel,
) {

    BaseLanguageSetupScreen(
        titleStringResource = titleStringResource,
        nextButtonStringResource = nextButtonStringResource,
        viewModel = viewModel,
    ) {
        val languages by viewModel.languages.collectAsState()
        val preSelectedLanguage by viewModel.preSelectedLanguage.collectAsState()

        LanguageChooser(
            modifier = Modifier.fillMaxSize(),
            languages = languages,
            selectedLanguage = preSelectedLanguage,
            onSelectLanguage = viewModel::onLanguageSelected,
        )
    }
}