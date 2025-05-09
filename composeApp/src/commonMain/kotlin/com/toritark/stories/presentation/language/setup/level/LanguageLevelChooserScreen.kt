package com.toritark.stories.presentation.language.setup.level

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.toritark.stories.presentation.core_ui.nav.OnNavigateTo
import com.toritark.stories.presentation.core_ui.nav.OnPopBackStack
import com.toritark.stories.presentation.language.setup.base.BaseLanguageSetupScreen
import com.toritark.stories.presentation.language.setup.level.model.LanguageLevelUiModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_choose_language_level_next_btn
import toritark.composeapp.generated.resources.title_choose_language_level_screen

@Composable
internal fun LanguageLevelChooserScreen(
    onNavigate: OnNavigateTo,
    onPopBackStack: OnPopBackStack,
    viewModel: LanguageLevelChooserViewModel = koinViewModel(),
) {
    viewModel.onNavigate = onNavigate
    viewModel.onPopBackStack = onPopBackStack

    BaseLanguageSetupScreen(
        titleStringResource = Res.string.title_choose_language_level_screen,
        nextButtonStringResource = Res.string.title_choose_language_level_next_btn,
        viewModel = viewModel,
    ) {
        val languageLevels by viewModel.languageLevels.collectAsState()
        val listState = rememberLazyListState()

        val selectedLanguageLevel = mutableStateOf<LanguageLevelUiModel?>(null)

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(languageLevels) { index, languageLevel ->
                LanguageLevelItem(
                    languageLevel = languageLevel,
                    isSelected = selectedLanguageLevel.value == languageLevel,
                    onClick = {
                        viewModel.onLanguageLevelSelected(it)
                        selectedLanguageLevel.value = it
                    },
                )

                if (index < languageLevels.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun LanguageLevelItem(
    languageLevel: LanguageLevelUiModel,
    isSelected: Boolean,
    onClick: (LanguageLevelUiModel) -> Unit,
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        Color.Transparent
    }
    val titleColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    val descriptionColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f)
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
            .clickable(onClick = { onClick(languageLevel) })
            .padding(all = 16.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(languageLevel.titleStringResource),
            style = MaterialTheme.typography.titleLarge,
            color = titleColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = stringResource(languageLevel.descriptionStringResource),
            style = MaterialTheme.typography.bodyLarge,
            color = descriptionColor,
        )
    }
}
