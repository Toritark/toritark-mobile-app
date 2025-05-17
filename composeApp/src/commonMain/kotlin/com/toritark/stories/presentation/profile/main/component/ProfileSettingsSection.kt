package com.toritark.stories.presentation.profile.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.toritark.stories.data.language.model.Language
import com.toritark.stories.data.language.model.LanguageLevel
import com.toritark.stories.presentation.core_ui.component.FlatCard
import com.toritark.stories.presentation.language.setup.level.model.LanguageLevelUiModel
import com.toritark.stories.presentation.main.app.AppTheme
import com.toritark.stories.presentation.profile.main.model.ProfileMainScreenState
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toritark.composeapp.generated.resources.*

@Composable
internal fun ProfileSettingsSection(
    modifier: Modifier = Modifier,
    languageSettingsState: ProfileMainScreenState.LanguageSettingsState,
    onChooseLearningLanguageClick: () -> Unit,
    onChooseLanguageLevelClick: () -> Unit,
    onChooseNativeLanguageClick: () -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        LanguagesSection(
            languageSettingsState = languageSettingsState,
            onChooseLearningLanguageClick = onChooseLearningLanguageClick,
            onChooseLanguageLevelClick = onChooseLanguageLevelClick,
            onChooseNativeLanguageClick = onChooseNativeLanguageClick,
        )
    }
}

@Composable
private fun LanguagesSection(
    languageSettingsState: ProfileMainScreenState.LanguageSettingsState,
    onChooseLearningLanguageClick: () -> Unit,
    onChooseLanguageLevelClick: () -> Unit,
    onChooseNativeLanguageClick: () -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current

    ProfileLanguageSettingsItem(
        modifier = Modifier
            .fillMaxWidth(),
        titleResource = Res.string.title_profile_settings_learning_language,
        language = (languageSettingsState as? ProfileMainScreenState.LanguageSettingsState.Present)?.learningLanguage,
        onClick = {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
            onChooseLearningLanguageClick()
        },
    )

    Spacer(modifier = Modifier.height(8.dp))

    LanguageLevelSettingsItem(
        modifier = Modifier
            .fillMaxWidth(),
        titleResource = Res.string.title_profile_settings_learning_language_level,
        languageLevel = (languageSettingsState as? ProfileMainScreenState.LanguageSettingsState.Present)?.languageLevel,
        onClick = {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
            onChooseLanguageLevelClick()
        },
    )

    Spacer(modifier = Modifier.height(8.dp))

    ProfileLanguageSettingsItem(
        modifier = Modifier
            .fillMaxWidth(),
        titleResource = Res.string.title_profile_settings_native_language,
        language = (languageSettingsState as? ProfileMainScreenState.LanguageSettingsState.Present)?.nativeLanguage,
        onClick = {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
            onChooseNativeLanguageClick()
        },
    )
}

@Composable
private fun ProfileLanguageSettingsItem(
    modifier: Modifier = Modifier,
    titleResource: StringResource,
    language: Language?,
    onClick: () -> Unit,
) {
    ProfileSettingsItem(
        modifier = modifier,
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            Text(
                modifier = Modifier,
                text = stringResource(titleResource),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier,
            ) {
                // Flag
                Text(
                    text = language?.flagUnicode ?: " ",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(end = 16.dp)
                )

                // Language details
                Column {
                    // Native name
                    Text(
                        text = language?.name ?: " ",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // English name
                    Text(
                        text = language?.nameEn ?: " ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun LanguageLevelSettingsItem(
    modifier: Modifier = Modifier,
    titleResource: StringResource,
    languageLevel: LanguageLevelUiModel?,
    onClick: () -> Unit,
) {
    ProfileSettingsItem(
        modifier = modifier,
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            Text(
                modifier = Modifier,
                text = stringResource(titleResource),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier,
            ) {
                // Level
                Text(
                    text = languageLevel?.titleStringResource?.let { stringResource(it) } ?: " ",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(end = 16.dp)
                )

                // Description
                Text(
                    text = languageLevel?.descriptionStringResource?.let { stringResource(it) } ?: " ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun ProfileSettingsItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    FlatCard(
        modifier = modifier,
        backgroundColor = MaterialTheme.colorScheme.background,
        borderColor = MaterialTheme.colorScheme.secondary,
        borderWidth = 1.dp,
        cornerRadius = 24.dp,
        rightIcon = Icons.Default.ChevronRight,
        rightIconTint = MaterialTheme.colorScheme.onBackground,
        padding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
        onClick = onClick,
        content = content,
    )
}

@Preview
@Composable
private fun ProfileSettingsSectionPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 600.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            ProfileSettingsSection(
                modifier = Modifier
                    .fillMaxWidth(),
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
                onChooseLearningLanguageClick = {},
                onChooseLanguageLevelClick = {},
                onChooseNativeLanguageClick = {},
            )
        }
    }
}