package com.toritark.app.presentation.language.setup.level

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.toritark.app.data.language.model.LanguageLevel
import com.toritark.app.presentation.core_ui.nav.OnNavigateTo
import com.toritark.app.presentation.core_ui.nav.OnPopBackStack
import com.toritark.app.presentation.language.setup.base.BaseLanguageSetupScreen
import com.toritark.app.presentation.language.setup.level.model.LanguageLevelChooserScreenState
import com.toritark.app.presentation.language.setup.level.model.LanguageLevelUiModel
import com.toritark.app.presentation.main.app.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import toritark.composeapp.generated.resources.*

@Composable
internal fun LanguageLevelChooserScreen(
    onNavigateTo: OnNavigateTo,
    onPopBackStack: OnPopBackStack,
    viewModel: LanguageLevelChooserViewModel = koinViewModel(),
) {
    viewModel.onNavigateTo = onNavigateTo
    viewModel.onPopBackStack = onPopBackStack

    BaseLanguageSetupScreen(
        titleStringResource = Res.string.title_choose_language_level_screen,
        nextButtonStringResource = Res.string.title_choose_language_level_next_btn,
        viewModel = viewModel,
    ) { screenState ->
        LanguageLevelChooserScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                )
                .statusBarsPadding()
                .padding(horizontal = 16.dp),
            screenState = screenState,
            onLanguageLevelSelected = viewModel::onLanguageLevelSelected,
        )
    }
}

@Composable
private fun LanguageLevelChooserScreenContent(
    modifier: Modifier = Modifier,
    screenState: LanguageLevelChooserScreenState,
    onLanguageLevelSelected: (languageLevel: LanguageLevelUiModel) -> Unit,
) {
    val listState = rememberLazyListState()

    val hapticFeedback = LocalHapticFeedback.current

    LazyColumn(
        state = listState,
        modifier = modifier,
    ) {
        itemsIndexed(screenState.levels) { index, languageLevel ->
            LanguageLevelItem(
                languageLevel = languageLevel,
                isSelected = screenState.selectedLevel == languageLevel,
                isFirst = index == 0,
                isLast = index == screenState.levels.lastIndex,
                onClick = {
                    onLanguageLevelSelected(languageLevel)

                    hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                },
            )

            if (index < screenState.levels.lastIndex) {
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
private fun LanguageLevelItem(
    languageLevel: LanguageLevelUiModel,
    isSelected: Boolean,
    isFirst: Boolean,
    isLast: Boolean,
    onClick: (LanguageLevelUiModel) -> Unit,
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.background
    }

    val textColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onBackground
    }

    val backgroundShape = when {
        isFirst && isLast -> RoundedCornerShape(
            size = 24.dp,
        )

        isFirst -> RoundedCornerShape(
            topStart = 24.dp,
            topEnd = 24.dp,
        )

        isLast -> RoundedCornerShape(
            bottomStart = 24.dp,
            bottomEnd = 24.dp,
        )

        else -> RoundedCornerShape(size = 4.dp)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = backgroundShape,
            )
            .clip(backgroundShape)
            .clickable(onClick = { onClick(languageLevel) })
            .padding(all = 16.dp)
    ) {
        Text(
            modifier = Modifier
                .width(48.dp)
                .align(Alignment.CenterVertically),
            text = stringResource(languageLevel.titleStringResource),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )

        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(languageLevel.descriptionStringResource),
            style = MaterialTheme.typography.bodyLarge,
            color = textColor,
        )
    }
}

@Preview
@Composable
private fun LanguageLevelItemPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 200.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            LanguageLevelItem(
                languageLevel = LanguageLevelUiModel(
                    languageLevel = LanguageLevel.A2,
                    titleStringResource = Res.string.title_language_level_a2,
                    descriptionStringResource = Res.string.desc_language_level_a2,
                ),
                isFirst = false,
                isLast = false,
                isSelected = false,
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun LanguageLevelChooserScreenContentPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 600.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp),
        ) {
            LanguageLevelChooserScreenContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                    ),
                screenState = LanguageLevelChooserScreenState(
                    levels = LanguageLevelUiModel.allLevels,
                    isNextButtonEnabled = true,
                ),
                onLanguageLevelSelected = {},
            )
        }
    }
}