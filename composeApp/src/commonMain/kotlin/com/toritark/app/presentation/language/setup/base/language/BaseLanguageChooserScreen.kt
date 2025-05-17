package com.toritark.app.presentation.language.setup.base.language

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.toritark.app.presentation.language.base.LanguageChooser
import com.toritark.app.presentation.language.setup.base.BaseLanguageSetupScreen
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
    ) { contentValue ->

        LanguageChooser(
            modifier = Modifier.fillMaxSize(),
            languages = contentValue.languages,
            selectedLanguage = contentValue.preSelectedLanguage,
            onSelectLanguage = viewModel::onLanguageSelected,
        )
    }
}