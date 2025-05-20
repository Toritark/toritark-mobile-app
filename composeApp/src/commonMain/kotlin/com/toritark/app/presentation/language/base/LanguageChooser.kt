package com.toritark.app.presentation.language.base

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.toritark.app.data.language.model.learningLanguages
import com.toritark.app.presentation.language.model.LanguageUiModel
import com.toritark.app.presentation.main.app.theme.AppTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_search_languages_field

@Composable
fun LanguageChooser(
    modifier: Modifier = Modifier,
    languages: List<LanguageUiModel>,
    selectedLanguage: LanguageUiModel?,
    onSelectLanguage: (LanguageUiModel) -> Unit,
) {
    val searchQuery = remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    var currentSelectedLanguage by remember(selectedLanguage) {
        mutableStateOf(selectedLanguage)
    }

    // Filter languages based on the search query
    val filteredLanguages by remember(languages, searchQuery.value) {
        derivedStateOf {
            val query = searchQuery.value.trim().lowercase()
            if (query.isEmpty()) {
                languages
            } else {
                languages.filter {
                    it.displayName.lowercase().contains(query) ||
                            it.language.name.lowercase().contains(query) ||
                            it.language.nameEn.lowercase().contains(query)
                }
            }
        }
    }

    // Scroll to selected language when the list is first composed
    LaunchedEffect(selectedLanguage, languages) {
        selectedLanguage?.let { selected ->
            val index = languages.indexOf(selected)
            if (index >= 0) {
                coroutineScope.launch {
                    listState.scrollToItem(index)
                }
            }
        }
    }

    Column(
        modifier = modifier,
    ) {
        // Sticky search field
        SearchField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )

        Spacer(modifier = Modifier.height(16.dp))

        val hapticFeedback = LocalHapticFeedback.current

        // Language list
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize(),
        ) {
            itemsIndexed(filteredLanguages) { index, language ->
                LanguageItem(
                    language = language,
                    isSelected = language == currentSelectedLanguage,
                    isFirst = index == 0,
                    isLast = index == filteredLanguages.lastIndex,
                    onClick = {
                        currentSelectedLanguage = language
                        onSelectLanguage(language)
                        // Hide keyboard when a language is selected
                        keyboardController?.hide()

                        hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                    }
                )

                if (index < filteredLanguages.lastIndex) {
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

@Composable
private fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextField(
        shape = RoundedCornerShape(24.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(Res.string.title_search_languages_field),
            )
        },
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = { Text(stringResource(Res.string.title_search_languages_field)) },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
        )
    )
}

@Composable
private fun LanguageItem(
    modifier: Modifier = Modifier,
    language: LanguageUiModel,
    isSelected: Boolean,
    isFirst: Boolean,
    isLast: Boolean,
    onClick: () -> Unit,
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
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = backgroundShape,
            )
            .clip(backgroundShape)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // Flag
            Text(
                text = language.language.flagUnicode,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(end = 16.dp)
            )

            // Language details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Display name
                Text(
                    text = language.displayName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = textColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // English name
                Text(
                    text = language.language.nameEn,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Check icon at the end
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun LanguageChooserPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 600.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            LanguageChooser(
                modifier = Modifier
                    .fillMaxWidth(),
                languages = learningLanguages.map {
                    LanguageUiModel(
                        language = it,
                        displayName = it.name,
                    )
                },
                selectedLanguage = LanguageUiModel(
                    language = learningLanguages[2],
                    displayName = learningLanguages[2].name,
                ),
                onSelectLanguage = {},
            )
        }
    }
}
