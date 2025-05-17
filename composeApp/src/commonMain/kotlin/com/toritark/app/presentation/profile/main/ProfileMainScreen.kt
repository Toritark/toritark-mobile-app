package com.toritark.app.presentation.profile.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.toritark.app.data.language.model.Language
import com.toritark.app.data.language.model.LanguageLevel
import com.toritark.app.presentation.core_ui.nav.OnNavigateTo
import com.toritark.app.presentation.core_ui.nav.OnPopBackStack
import com.toritark.app.presentation.core_ui.screen.BaseScreen
import com.toritark.app.presentation.language.setup.level.model.LanguageLevelUiModel
import com.toritark.app.presentation.main.app.AppTheme
import com.toritark.app.presentation.profile.main.component.ProfileHeaderSection
import com.toritark.app.presentation.profile.main.component.ProfileSettingsSection
import com.toritark.app.presentation.profile.main.model.ProfileMainScreenState
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.desc_language_level_a2
import toritark.composeapp.generated.resources.title_language_level_a2

@Composable
internal fun ProfileMainScreen(
    onNavigateTo: OnNavigateTo,
    onPopBackStack: OnPopBackStack,
    viewModel: ProfileMainViewModel = koinViewModel(),
) {
    // TODO: Profile Screen
    viewModel.onNavigateTo = onNavigateTo
    viewModel.onPopBackStack = onPopBackStack

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    BaseScreen(
        viewModel = viewModel,
    ) { contentValue ->
        ProfileScreenContent(
            modifier = Modifier.fillMaxSize(),
            contentValue = contentValue,
            onChooseLearningLanguageClick = viewModel::onChooseLearningLanguageClick,
            onChooseLanguageLevelClick = viewModel::onChooseLanguageLevelClick,
            onChooseNativeLanguageClick = viewModel::onChooseNativeLanguageClick,
        )
    }
}

@Composable
private fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    contentValue: ProfileMainScreenState,
    onChooseLearningLanguageClick: () -> Unit,
    onChooseLanguageLevelClick: () -> Unit,
    onChooseNativeLanguageClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.size(16.dp))


        ProfileHeaderSection(
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.size(24.dp))

        ProfileSettingsSection(
            modifier = Modifier.fillMaxWidth(),
            languageSettingsState = contentValue.languageSettingsState,
            onChooseLearningLanguageClick = onChooseLearningLanguageClick,
            onChooseLanguageLevelClick = onChooseLanguageLevelClick,
            onChooseNativeLanguageClick = onChooseNativeLanguageClick,
        )
    }
}

@Preview
@Composable
private fun ProfileScreenContentPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 800.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            ProfileScreenContent(
                modifier = Modifier
                    .fillMaxWidth(),
                contentValue = ProfileMainScreenState(
                    languageSettingsState = ProfileMainScreenState.LanguageSettingsState.Present(
                        learningLanguage = Language(
                            isoCode = "en",
                            nameEn = "English",
                            name = "English",
                            flagUnicode = "\uD83C\uDDEC\uD83C\uDDE7",
                        ),
                        languageLevel = LanguageLevelUiModel(
                            languageLevel = LanguageLevel.A2,
                            titleStringResource = Res.string.title_language_level_a2,
                            descriptionStringResource = Res.string.desc_language_level_a2,
                        ),
                        nativeLanguage = Language(
                            isoCode = "et",
                            nameEn = "Estonian",
                            name = "Eesti keel",
                            flagUnicode = "\uD83C\uDDEA\uD83C\uDDEA",
                        ),
                    ),
                ),
                onChooseLearningLanguageClick = {},
                onChooseLanguageLevelClick = {},
                onChooseNativeLanguageClick = {},
            )
        }
    }
}