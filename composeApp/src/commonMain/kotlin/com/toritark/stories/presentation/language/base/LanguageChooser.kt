package com.toritark.stories.presentation.language.base

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.toritark.stories.data.language.model.Language
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_search_languages_field

@Composable
fun LanguageChooser(
    modifier: Modifier = Modifier,
    languages: List<Language>,
    selectedLanguage: Language?,
    onSelectLanguage: (Language) -> Unit,
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
                    it.name.lowercase().contains(query) ||
                            it.nameEn.lowercase().contains(query)
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

    Column(modifier = modifier) {
        // Sticky search field
        SearchField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .focusRequester(focusRequester)
        )

        // Language list
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredLanguages) { language ->
                LanguageItem(
                    language = language,
                    isSelected = language == currentSelectedLanguage,
                    onClick = {
                        currentSelectedLanguage = language
                        onSelectLanguage(language)
                        // Hide keyboard when a language is selected
                        keyboardController?.hide()
                    }
                )
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
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = { Text(stringResource(Res.string.title_search_languages_field)) },
        singleLine = true
    )
}

@Composable
private fun LanguageItem(
    language: Language,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.background
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Flag
        Text(
            text = language.flagUnicode,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(end = 16.dp)
        )

        // Language details
        Column {
            // Native name
            Text(
                text = language.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // English name
            Text(
                text = language.nameEn,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
