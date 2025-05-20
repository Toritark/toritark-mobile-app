package com.toritark.app.presentation.language.setup.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.toritark.app.data.language.model.learningLanguages
import com.toritark.app.presentation.core_ui.screen.BaseScreen
import com.toritark.app.presentation.language.base.LanguageChooser
import com.toritark.app.presentation.language.model.LanguageUiModel
import com.toritark.app.presentation.language.setup.base.language.model.LanguageChooserScreenState
import com.toritark.app.presentation.language.setup.base.model.BaseLanguageSetupScreenState
import com.toritark.app.presentation.main.app.theme.AppTheme
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_choose_learning_language_next_btn
import toritark.composeapp.generated.resources.title_choose_learning_language_screen

@Composable
internal fun <C : BaseLanguageSetupScreenState> BaseLanguageSetupScreen(
    titleStringResource: StringResource,
    nextButtonStringResource: StringResource,
    viewModel: BaseLanguageSetupViewModel<C>,
    content: @Composable (screenState: C) -> Unit,
) {
    BaseScreen(viewModel) { screenState ->
        BaseLanguageSetupScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                )
                .statusBarsPadding(),
            titleStringResource = titleStringResource,
            nextButtonStringResource = nextButtonStringResource,
            screenState = screenState,
            isNextButtonEnabled = screenState.isNextButtonEnabled,
            onNextButtonClick = viewModel::onNextButtonClick,
            content = content,
        )
    }
}

@Composable
private fun <C : BaseLanguageSetupScreenState> BaseLanguageSetupScreenContent(
    modifier: Modifier = Modifier,
    titleStringResource: StringResource,
    nextButtonStringResource: StringResource,
    screenState: C,
    isNextButtonEnabled: Boolean,
    onNextButtonClick: () -> Unit,
    content: @Composable (screenState: C) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = stringResource(titleStringResource),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.headlineSmall.copy(textAlign = TextAlign.Center),
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            content(screenState)
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = onNextButtonClick,
            enabled = isNextButtonEnabled,
        ) {
            Text(
                text = stringResource(nextButtonStringResource),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 12.dp),
            )
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowForward,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.size(width = 0.dp, height = 16.dp))
    }
}

@Preview
@Composable
private fun BaseLanguageSetupScreenPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 700.dp)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            BaseLanguageSetupScreenContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                titleStringResource = Res.string.title_choose_learning_language_screen,
                nextButtonStringResource = Res.string.title_choose_learning_language_next_btn,
                screenState = LanguageChooserScreenState(
                    languages = previewLanguages,
                ),
                isNextButtonEnabled = true,
                onNextButtonClick = {},
            ) {
                LanguageChooser(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    languages = previewLanguages,
                    selectedLanguage = previewLanguages.first(),
                    onSelectLanguage = {},
                )
            }
        }
    }
}

private val previewLanguages by lazy {
    learningLanguages.map { LanguageUiModel(it, it.name) }
}