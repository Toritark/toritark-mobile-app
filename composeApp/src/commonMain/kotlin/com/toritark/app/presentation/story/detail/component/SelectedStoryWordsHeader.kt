package com.toritark.app.presentation.story.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.toritark.app.presentation.core_ui.component.FlatCard
import com.toritark.app.presentation.main.app.AppTheme
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_story_selected_words
import toritark.composeapp.generated.resources.title_story_selected_words_add_to_learning
import toritark.composeapp.generated.resources.title_story_selected_words_remove_all

@Composable
internal fun SelectedStoryWordsHeader(
    modifier: Modifier = Modifier,
    words: Set<String>,
    onClearClick: () -> Unit,
    onAddAllClick: () -> Unit,
) {
    FlatCard(
        modifier = modifier,
        padding = PaddingValues(top = 16.dp),
        backgroundColor = MaterialTheme.colorScheme.background,
        cornerRadius = 24.dp,
        borderColor = MaterialTheme.colorScheme.secondary,
        borderWidth = 1.dp,
    ) {
        Column {
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                text = pluralStringResource(
                    Res.plurals.title_story_selected_words,
                    words.size,
                    words.size,
                ),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )

            ButtonsRow(
                onClearClick = onClearClick,
                onAddAllClick = onAddAllClick,
            )
        }
    }
}

@Composable
private fun ButtonsRow(
    onClearClick: () -> Unit,
    onAddAllClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
    ) {
        TextButton(
            colors = ButtonDefaults.textButtonColors().copy(
                contentColor = MaterialTheme.colorScheme.secondary,
            ),
            onClick = onClearClick,
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Default.Clear,
                contentDescription = null,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(Res.string.title_story_selected_words_remove_all),
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        TextButton(
            onClick = onAddAllClick,
        ) {
            Text(
                text = stringResource(Res.string.title_story_selected_words_add_to_learning),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.AutoMirrored.Default.ArrowForward,
                contentDescription = null,
            )
        }
    }
}

@Preview
@Composable
private fun SelectedStoryWordsHeaderPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .size(width = 400.dp, height = 400.dp)
                .padding(16.dp),
        ) {
            SelectedStoryWordsHeader(
                words = setOf("Hello", "World", "Testing", "Multi-line", "Text"),
                onClearClick = {},
                onAddAllClick = {},
            )
        }
    }
}